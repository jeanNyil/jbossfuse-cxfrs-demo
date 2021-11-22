package org.jeannyil.fuse.demo.ipservice.server.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.jeannyil.fuse.demo.ipservice.server.constants.ErrorTypesEnum;
import org.jeannyil.fuse.demo.ipservice.server.constants.UtilHeadersEnum;
import org.jeannyil.fuse.demo.ipservice.server.exception.TypeValidationException;

public class IpServiceCheckRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Error handling using Camel DefaultErrorHandler
        onException(TypeValidationException.class)
                .handled(true) // Flag exception as handled
                .logStackTrace(true)
                .logExhausted(true)
                .logHandled(true)
                .doTry()
                .setProperty(UtilHeadersEnum.ERRORTYPE.toString(), constant(ErrorTypesEnum.VALIDATION_ERROR.toString()))
                // Set the exception message and build the ErrorBean
                .transform().simple("${exception.message}")
                .process("buildErrorBeanProcessor")
                // Transform the ErrorBean message to JSON format
                .marshal().json(JsonLibrary.Jackson, true)
                // Prepare and send a validation exception RESTful response to caller
                .process("prepareRestResponseProcessor");
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
                // Prepare and send an exception RESTful response to caller
                .process("prepareRestResponseProcessor");

        /**
         *  Route that implements the checkRequesterLocation REST service operation.
         *  This route calls the remote Ipstack API and returns the raw response
         */
        from("direct:checkRequesterLocation")
                .routeId("{{camel.name.route}}-checkRequesterLocation")
                .log(LoggingLevel.INFO, "headers: ${headers}")
                .log(LoggingLevel.INFO, "Starting the checkRequesterLocation RESTful service operation...")
                .process("validateRequestProcessor") // Validate input request
                .setProperty("operationName", header("operationName"))
                .removeHeaders("*", "breadcrumbId") // Reset all the exchange message headers (except breadcrumbId) before proceeding
                .process("ipstackAPIRequestProcessor")
                .to("cxfrs:bean:ipstackApiRsClient")
                .log(LoggingLevel.INFO, "Ipstack API Response:\n ${body}")
                .log(LoggingLevel.INFO, "The checkRequesterLocation RESTful service operation is DONE");
    }
}
