package org.jeannyil.fuse.demo.ipservice.server.processor;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.jeannyil.fuse.demo.ipservice.server.constants.GeoLocationParametersEnum;

public class IpstackAPIRequestProcessor implements Processor {

    private static final String ACCESS_KEY = "000600aef2c07ed3cf4136a1f5443328";

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.setPattern(ExchangePattern.InOut);
        Message inMessage = exchange.getIn();
        String operationName = exchange.getProperty("operationName",String.class);
        
        //creating the request
        MessageContentsList req = new MessageContentsList();
        
        if (operationName.equalsIgnoreCase("getGeoLocation")) {
            req.add(exchange.getProperty(GeoLocationParametersEnum.IP.toString(),String.class)); // ip or domain URL to lookup
            // set the operation name to getGeoIp
            inMessage.setHeader(CxfConstants.OPERATION_NAME, "getGeoIp");
        } else {
            // set the operation name to getRequesterLocation
            inMessage.setHeader(CxfConstants.OPERATION_NAME, "getRequesterLocation");
        }
        req.add(ACCESS_KEY); // access key to the Ipstack API plan
        req.add(exchange.getProperty(GeoLocationParametersEnum.OUTPUT.toString(),String.class)); // requested output format
        
        // using the proxy client API
        inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);
        // Put the request in the message body
        inMessage.setBody(req);
	}	
}
