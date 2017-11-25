package org.jeannyil.fuse.demo.ipservice.server.resource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/")
@Api(value = "/", description = "RESTful service that exposes operations to retrieve an IP or hostname geo-location")
public class IpService {
	
	@GET
	@Path("/geolocation")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@ApiOperation(value = "Get IP or hostname geo-location", 
				  notes = "The following matrix parameters can optionnally be supplied: type, ip\n" +
				  		  "The response format depends on the input type parameter: xml or json", 
				  response = Response.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 500, message = "Invalid Type or IP supplied"),
	        @ApiResponse(code = 204, message = "IP not found")
	        })
	public Response getGeoLocation(@ApiParam(value = "Expected response format: xml or json - default: json", required = false) @MatrixParam("type") @DefaultValue("json") String type,
								  @ApiParam(value = "IP address or hostname - default: empty", required = false) @MatrixParam("ip") @DefaultValue("") String ip) {
		return null;
	} 

}
