package org.jeannyil.fuse.demo.ipservice.client.processor;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.MessageContentsList;
import org.jeannyil.fuse.demo.ipservice.client.constants.IpServiceInputParametersEnum;

public class IpServiceGetGeoLocationRequestProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		exchange.setPattern(ExchangePattern.InOut);
        Message inMessage = exchange.getIn();
         
        //creating the request
        String ipOrHostname = inMessage.getHeader(IpServiceInputParametersEnum.IP.toString(), String.class);
        String expectedResponseFormat = inMessage.getHeader(IpServiceInputParametersEnum.TYPE.toString(),String.class);
        	
        MessageContentsList req = new MessageContentsList();
        req.add(expectedResponseFormat);
        req.add(ipOrHostname);
        
        // set the operation name
        inMessage.setHeader(CxfConstants.OPERATION_NAME, "getGeoLocation");
        // using the proxy client API
        inMessage.setHeader(CxfConstants.CAMEL_CXF_RS_USING_HTTP_API, Boolean.FALSE);
        // Put the request in the message body
        inMessage.setBody(req);
	}

}
