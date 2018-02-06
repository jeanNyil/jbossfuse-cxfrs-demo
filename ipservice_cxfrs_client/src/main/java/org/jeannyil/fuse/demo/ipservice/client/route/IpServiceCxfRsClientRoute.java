package org.jeannyil.fuse.demo.ipservice.client.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.jeannyil.fuse.demo.ipservice.client.constants.IpServiceInputParametersEnum;

public class IpServiceCxfRsClientRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Error handling
		errorHandler(defaultErrorHandler().maximumRedeliveries(0));
		onException(org.everit.json.schema.ValidationException.class)
			.handled(false) // in order to allow the FileComponent moveFailed execution
			.logStackTrace(true)
			.logHandled(true)
			.logExhausted(false)
			.log(LoggingLevel.ERROR, "The content of [${file:name}] file is NOT VALID JSON: ${exception}")
			// Send the error message to the JBoss A-MQ broker destination
			.transform(simple("The content of [${file:name}] file is NOT VALID JSON: ${exception}"))
            .removeHeaders("*")
            .setHeader("ProcessedFile", exchangeProperty("ProcessedFile"))
			.to(ExchangePattern.InOnly,"amq:{{notif.amq.destination}}?timeToLive={{output.message.ttl.inms}}");
		onException(Exception.class)
			.handled(false) // in order to allow the FileComponent moveFailed execution
			.logStackTrace(true)
			.logExhausted(true)
			.logHandled(true)
			// Send the error message to the JBoss A-MQ broker destination
			.transform(simple("Unexpected exception while processing [${file:name}]: ${exception}"))
			.removeHeaders("*")
			.setHeader("ProcessedFile", exchangeProperty("ProcessedFile"))
			.to(ExchangePattern.InOnly,"amq:{{notif.amq.destination}}?timeToLive={{output.message.ttl.inms}}");

		/**
		 *  Route that consumes a file containing a list of input parameter for the RESTful IpService in JSON format
		 *  First, the JSON file is validated by leveraging the Everit JSON Schema validator 
		 *  (https://github.com/everit-org/json-schema) before calling the RESTful IpService according
		 *  to the size of the parameters list.
		 *  The IpService RESTful response for each request is sent an AMQ broker destination
		 *  
		 *  Example of a file JSON data:
		 *  {
		 *	 "parameters": [
		 *	    {
		 *	      "ip_or_hostname": "",
		 *	      "response_type": "xml"
		 *	    },
		 *	    {
		 *	      "ip_or_hostname": "redhat.com",
		 *	      "response_type": "json"
		 *	    }
		 *	  ]
		 *	}
		 */
		from("file:{{json.file.input.path}}?charset=utf-8&delete=true&readLock=changed" + 
			 "&readLockTimeout=30000&readLockCheckInterval=10000" + 
			 "&extendedAttributes=basic:creationTime&moveFailed={{json.file.error.path}}")
			.routeId("{{camel.name.route}}")
			.log(LoggingLevel.INFO, "Processing consumed JSON file [${file:name}]...")
            .log(LoggingLevel.INFO, "File content:  ${body}")
            .setProperty("ProcessedFile", simple("${file:name}"))
			// Validate JSON content against the expected JSON schema
			.setProperty("jsonSchema", simple("Schemas/inputMessageSchema.json"))
			.bean("jsonValidatorBean", "validateJsonWithEverit(${body},${header.jsonSchema})")
			// TODO - Use file content to create RESTful request parameters
            .removeHeaders("*") // Reset all the exchange message headers before proceeding to CXFRS client component
			.setHeader(IpServiceInputParametersEnum.TYPE.toString(), constant("json"))
			.setHeader(IpServiceInputParametersEnum.IP.toString(), constant("redhat.com"))
			.log(LoggingLevel.INFO, "Calling the IP Service getGeoLocation operation...")
			.process("ipServiceGetGeoLocationRequestProcessor")
			.to("cxfrs:bean:ipServiceRsClient")
			.log(LoggingLevel.INFO, "IP Service getGeoLocation operation response : ${body}")
			// Send the response to a JBoss A-MQ broker destination
            .removeHeaders("*")
            .setHeader("ProcessedFile", exchangeProperty("ProcessedFile"))
			.to(ExchangePattern.InOnly, "amq:{{notif.amq.destination}}?timeToLive={{output.message.ttl.inms}}");
	}
}
