package org.jeannyil.fuse.demo.ipservice.server.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.jeannyil.fuse.demo.ipservice.server.constants.ErrorTypesEnum;
import org.jeannyil.fuse.demo.ipservice.server.constants.GeoLocationParametersEnum;
import org.jeannyil.fuse.demo.ipservice.server.exception.TypeValidationException;

public class IpServiceGeoLocationRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Error handling
        errorHandler(defaultErrorHandler().maximumRedeliveries(0));
        onException(TypeValidationException.class)
                .handled(true) // Flag exception as handled
                .logStackTrace(true)
                .logExhausted(true)
                .logHandled(true)
                .doTry()
                .setProperty("errorType", constant(ErrorTypesEnum.VALIDATION_ERROR.toString()))
                // Set the exception message and build the ErrorBean
                .transform().simple("${exception.message}")
                .process("buildErrorBeanProcessor")
                // Transform the ErrorBean message to JSON format
                .marshal().json(JsonLibrary.Jackson, true);
        onException(Exception.class)
                .handled(true) // Suppressing exception rethrow to the caller
                .logStackTrace(true)
                .logExhausted(true)
                .logHandled(true)
                .setProperty("errorType", constant(ErrorTypesEnum.ALLOTHER_ERROR.toString()))
                // Set the exception message and build the ErrorBean
                .transform().simple("${exception.message}")
                .process("buildErrorBeanProcessor")
                // Transform the ErrorBean message to JSON format
                .marshal().json(JsonLibrary.Jackson, true);

        /**
         *  Route that implements the getGeoLocation REST service operation.
         *  This route calls the remote FreeGeoIp RESTful service and returns the raw response
         */
        from("direct:getGeoLocation")
                .routeId("{{camel.name.route}}-getGeoLocation")
                .log(LoggingLevel.INFO, "Starting the getGeoLocation RESTful service operation...")
                // Validate input request
                .process("validateRequestProcessor")
                .setProperty(GeoLocationParametersEnum.TYPE.toString(),
                        header(GeoLocationParametersEnum.TYPE.toString()))
                .setProperty(GeoLocationParametersEnum.IP.toString(),
                        header(GeoLocationParametersEnum.IP.toString()))
                .removeHeaders("*") // Reset all the exchange message headers before proceeding to CXFRS client component
                .setHeader(GeoLocationParametersEnum.TYPE.toString(),
                        exchangeProperty(GeoLocationParametersEnum.TYPE.toString()))
                .setBody(exchangeProperty(GeoLocationParametersEnum.IP.toString()))
                .process("freeGeoIpRequestProcessor")
                .to("cxfrs:bean:freeGeoIpRsClient")
                .log(LoggingLevel.INFO, "FreeGeoIp Service Response:\n ${body}")
                // TODO later: marshal to Jaxb ou Json according to the request type :-D
                /*.choice()
                    .when(header("type").contains("json"))
                        .marshal().json(JsonLibrary.Jackson, true) // transform the request to JSON :-D
                        .log(LoggingLevel.INFO, "Returned JSON message: ${body}")
                    .otherwise()
                        .marshal().jaxb(true) //transform the request to XML :-D
                        .log(LoggingLevel.INFO, "Returned XML message: ${body}")
                .end()*/
                .log(LoggingLevel.INFO, "The getGeoLocation RESTful service operation is DONE");
    }
}
