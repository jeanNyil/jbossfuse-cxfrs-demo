package org.jeannyil.fuse.demo.ipservice.server.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/ipservice")
@Api(value = "/ipservice", description = "RESTful service that handles operations on an IP address or a hostname")
public class IpService {

	@GET
	@Path("/geolocation")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@ApiOperation(httpMethod = "GET",
				  value = "Get the geo-location for an IP address or a hostname",
				  notes = "The following optional query parameters can be supplied: type, ip<br>" +
				  		  "The response format depends on the input type parameter: xml or json<br>" +
						  "Request URI sample: /ipservice/geolocation?ip=redhat.com&type=json<br>" +
				  		  "Corresponding JSON response:<br>" +
						  "{<br/>" +
						  "    \"ip\": \"209.132.183.105\",<br>" +
						  "    \"country_code\": \"US\",<br>" +
						  "    \"country_name\": \"United States\",<br>" +
						  "    \"region_code\": \"NC\",\n" +
						  "    \"region_name\": \"North Carolina\",<br>" +
						  "    \"city\": \"Raleigh\",<br>" +
						  "    \"zip_code\": \"27601\",<br>" +
						  "    \"time_zone\": \"America/New_York\",<br>" +
						  "    \"latitude\": 35.7716,<br>" +
						  "    \"longitude\": -78.6356,<br>" +
						  "    \"metro_code\": 560<br>" +
						  "}", 
				  response = String.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 400, message = "Invalid input parameters"),
	        @ApiResponse(code = 500, message = "Internal server error")
	        })
	public Response getGeoLocation(@ApiParam(value = "Expected response format: xml or json - default: json", required = false) @QueryParam("type") @DefaultValue("json") String type,
								   @ApiParam(value = "IP address or hostname - default: empty", required = false) @QueryParam("ip") @DefaultValue("") String ip) {
		return null;
	}

}
