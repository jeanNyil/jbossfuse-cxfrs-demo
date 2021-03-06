package org.jeannyil.fuse.demo.ipservice.server.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.jeannyil.fuse.demo.ipservice.server.constants.ErrorTypesEnum;
import org.jeannyil.fuse.demo.ipservice.server.constants.UtilHeadersEnum;

public class IpServiceCxfRsServerMainRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		// Error handling using Camel DefaultErrorHandler
		onException(Exception.class)
				.handled(true) // Suppressing exception rethrow to the caller
				.logStackTrace(true)
				.logExhausted(true)
				.logHandled(true)
				.setProperty(UtilHeadersEnum.ERRORTYPE.toString(), constant(ErrorTypesEnum.ALLOTHER_ERROR.toString()))
				// Set the exception message and build the ErrorBean
				.transform().simple("${exception.message}")
				.process("buildErrorBeanProcessor")
				// Transform the ErrorBean message to JSON format
				.marshal().json(JsonLibrary.Jackson, true)
				// Wire-tap the JSON message to AMQ broker destination
				.wireTap("amq:{{error.amq.destination}}?timeToLive={{output.message.ttl.inms}}")
				// Prepare and send an exception RESTful response to caller
				.process("prepareRestResponseProcessor");

		/**
		 *  Route that consumes the RESTful service requests and routes them to the appropriate
		 *  operation for processing.
		 *  The REST service operation is then wire-tapped to a JMS queue on a secured JBoss A-MQ broker
		 *  instance before being transformed into a RESTful (JAX-RS) response through a camel processor.
		 */
		from("cxfrs:bean:ipRsService?bindingStyle=SimpleConsumer")
			.routeId("{{camel.name.route}}-main")
			.streamCaching() // Enable stream-caching
			.log(LoggingLevel.INFO, "Received RESTful request - Headers: ${headers} \n body: ${body}")
			.recipientList(simple("direct:${header.operationName}"))
			.wireTap("amq:{{notif.amq.destination}}?timeToLive={{output.message.ttl.inms}}")
			// Prepare successful RESTful response
			.process("prepareRestResponseProcessor");
	}

}
