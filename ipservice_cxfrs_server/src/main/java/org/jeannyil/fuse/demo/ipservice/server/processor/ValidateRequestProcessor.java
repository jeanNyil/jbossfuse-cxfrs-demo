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
		String type = inMessage.getHeader(GeoLocationParametersEnum.TYPE.toString(),String.class);
        // Throw validationException if the input request type parameter is not valid
        if (!validateInputFormatType(type)) {
        		exchange.setProperty(GeoLocationParametersEnum.TYPE.toString(), HandledOutputFormatsEnum.JSON.toString());
        		exchange.setProperty(GeoLocationParametersEnum.IP.toString(),
        							 inMessage.getHeader(GeoLocationParametersEnum.IP.toString(), String.class));
        		throw new TypeValidationException("[" + type + "] parameter is not valid value for type parameter! - possible values: " +
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
