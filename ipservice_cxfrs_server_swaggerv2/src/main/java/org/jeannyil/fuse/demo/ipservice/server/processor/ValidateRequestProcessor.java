package org.jeannyil.fuse.demo.ipservice.server.processor;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.jeannyil.fuse.demo.ipservice.server.constants.GeoLocationParametersEnum;
import org.jeannyil.fuse.demo.ipservice.server.constants.HandledOutputFormatsEnum;
import org.jeannyil.fuse.demo.ipservice.server.exception.TypeValidationException;

public class ValidateRequestProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		Message inMessage = exchange.getIn();
		String ip = inMessage.getHeader(GeoLocationParametersEnum.IP.toString(),String.class);
		String output = inMessage.getHeader(GeoLocationParametersEnum.OUTPUT.toString(),String.class);
		String operationName = inMessage.getHeader("operationName",String.class);

		// Keep the input parameters on the Exchange object
		exchange.setProperty(GeoLocationParametersEnum.IP.toString(), ip);
		exchange.setProperty(GeoLocationParametersEnum.OUTPUT.toString(), output);
		
		// Throw validationException if the input request [ip] query parameter is missing for the getGeoLocation operation
        if (operationName.equalsIgnoreCase("getGeoLocation")) {
			if (ip.isEmpty()) {
				exchange.setProperty(GeoLocationParametersEnum.OUTPUT.toString(), output);
				throw new TypeValidationException("[ip] query parameter is REQUIRED!");
			}
        }
		
		// Throw validationException if the input request [output] query parameter is not valid
        if (!validateInputFormatType(output)) {
			throw new TypeValidationException("[" + output + "] parameter is not valid value for type parameter! - possible values: " +
											  Arrays.asList(HandledOutputFormatsEnum.values()));
        }
	}
	
	/**
	 * Verifies the input request type parameter is handled
	 * @param inputFormatType
	 * @return true or false
	 */
	private boolean validateInputFormatType(String inputFormatType) {
		for (HandledOutputFormatsEnum format : HandledOutputFormatsEnum.values()) {
			if (format.getHandledType().equalsIgnoreCase(inputFormatType))
				return true;
		}
		
		return false;
	}

}
