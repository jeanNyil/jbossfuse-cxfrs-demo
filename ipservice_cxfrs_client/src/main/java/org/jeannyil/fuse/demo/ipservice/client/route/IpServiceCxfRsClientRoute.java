package org.jeannyil.fuse.demo.ipservice.client.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.jeannyil.fuse.demo.ipservice.client.constants.JMSPropertiesEnum;


public class IpServiceCxfRsClientRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Error handling using the Default
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
                .to(ExchangePattern.InOnly,"amq:{{error.amq.destination}}?timeToLive={{output.message.ttl.inms}}");
        onException(Exception.class)
                .handled(false) // in order to allow the FileComponent moveFailed execution
                .logStackTrace(true)
                .logExhausted(true)
                .logHandled(true)
                // Send the error message to the JBoss A-MQ broker destination
                .transform(simple("Unexpected exception while processing [${exchangeProperty.ProcessedFile}]: ${exception.stacktrace}"))
				.removeHeaders("*")
                .setHeader(JMSPropertiesEnum.PROCESSEDFILE.toString(),
                        exchangeProperty(JMSPropertiesEnum.PROCESSEDFILE.toString()))
                .setHeader(JMSPropertiesEnum.PROCESSEDPARAMETERS.toString(),
                        exchangeProperty(JMSPropertiesEnum.PROCESSEDPARAMETERS.toString()))
                .to(ExchangePattern.InOnly,"amq:{{error.amq.destination}}?timeToLive={{output.message.ttl.inms}}");

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
				"&extendedAttributes=basic:creationTime&include=.*.json" +
				"&moveFailed={{json.file.error.path}}")
                .routeId("{{camel.name.route}}")
                .log(LoggingLevel.INFO, "Processing consumed JSON file [${file:name}]...")
                .log(LoggingLevel.INFO, "File content: ${body}")
                .setProperty(JMSPropertiesEnum.PROCESSEDFILE.toString(), simple("${file:name}"))

                // Validate JSON content against the expected JSON schema
                .setProperty("jsonSchema", simple("Schemas/inputMessageSchema.json"))
                .bean("jsonValidatorBean", "validateJsonWithEverit(${body},${header.jsonSchema})")

                // Use of the splitter EIP to call the IP Geolocation RESTful service for each parameters set
                .split().jsonpath("$..parameters.*").streaming()
                    .log(LoggingLevel.INFO, "Split #${header.CamelSplitIndex} parameters: ${body}")
                    .setProperty(JMSPropertiesEnum.PROCESSEDPARAMETERS.toString(), bodyAs(String.class))
                    .removeHeaders("*") // Reset all the exchange message headers before proceeding
                    // Prepare IP Geolocation RESTful service request
                    .process("ipServiceGetGeoLocationRequestProcessor")
                    .log(LoggingLevel.INFO, "Calling the IP Service getGeoLocation operation...")
                    .to("cxfrs:bean:ipServiceRsClient")
                    .log(LoggingLevel.INFO, "IP Service getGeoLocation operation response : ${body}")
                    // Send the response to a JBoss A-MQ broker destination
                    .removeHeaders("*")
                    .setHeader(JMSPropertiesEnum.PROCESSEDFILE.toString(),
							exchangeProperty(JMSPropertiesEnum.PROCESSEDFILE.toString()))
                    .setHeader(JMSPropertiesEnum.PROCESSEDPARAMETERS.toString(),
							exchangeProperty(JMSPropertiesEnum.PROCESSEDPARAMETERS.toString()))
                    .to(ExchangePattern.InOnly, "amq:{{notif.amq.destination}}?timeToLive={{output.message.ttl.inms}}")
                .end()

                .log(LoggingLevel.INFO, "Processing JSON file [${exchangeProperty.ProcessedFile}] DONE!")
        ;
	}
}
