package org.jeannyil.fuse.demo.ipservice.server.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path(value="/ipservice")
@Api(value = "/ipservice", description = "RESTful service that handles operations on an IP address or a hostname")
public class IpService {

	@GET
	@Path(value="/geolocation")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@ApiOperation(httpMethod = "GET",
				  value = "Get the geo-location for an IP address or a hostname",
				  notes = "The following query parameters can be supplied: type, ip<br>" +
				  		  "The response format depends on the input type parameter: xml or json<br>" +
						  "Request URI sample: /ipservice/geolocation?ip=redhat.com&type=json<br>" +
				  		  "Corresponding JSON response:<br>" +
						  "{<br/>" +
						  "    \"city\": \"Raleigh\",<br>" +
						  "    \"continent_code\": \"NA\",<br>" +
						  "    \"continent_name\": \"North America\",<br>" +
						  "    \"country_code\": \"US\",<br>" +
						  "    \"country_name\": \"United States\",<br>" +
						  "    \"ip\": \"209.132.183.105\",<br>" +
						  "    \"latitude\": 35.775211334228516,<br>" +
						  "    \"location\": {<br>" +
						  "        \"calling_code\": \"1\",<br>" +
						  "        \"capital\": \"Washington D.C.\",<br>" +
						  "        \"country_flag\": \"http://assets.ipstack.com/flags/us.svg\",<br>" +
						  "        \"country_flag_emoji\": \"🇺🇸\",<br>" +
						  "        \"country_flag_emoji_unicode\": \"U+1F1FA U+1F1F8\",<br>" +
						  "        \"geoname_id\": 4487042,<br>" +
						  "        \"is_eu\": false,<br>" +
						  "        \"languages\": [<br>" +
						  "            {<br>" +
						  "                \"code\": \"en\",<br>" +
						  "                \"name\": \"English\",<br>" +
						  "                \"native\": \"English\"<br>" +
						  "            }<br>" +
						  "        ]<br>" +
						  "    },<br>" +
						  "    \"longitude\": -78.63432312011719,<br>" +
						  "    \"region_code\": \"NC\",<br>" +
						  "    \"region_name\": \"North Carolina\",<br>" +
						  "    \"type\": \"ipv4\",<br>" +
						  "    \"zip\": \"27601\"<br>" +
						  "}", 
				  response = String.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 400, message = "Invalid input parameters"),
	        @ApiResponse(code = 500, message = "Internal server error")
	        })
	public Response getGeoLocation(@ApiParam(value = "Any IPv4 or IPv6 address; you can also enter a domain URL to get a resolution of the underlying IP address - required", required = true) @DefaultValue("") @QueryParam("ip") String ip,
								   @ApiParam(value = "Expected response format: xml or json - default: json", required = false) @QueryParam("output") @DefaultValue("json") String outputFormat) {
		return null;
	}

	@GET
	@Path(value="/check")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@ApiOperation(httpMethod = "GET",
				  value = "Get the geo-location of the requester based on his IP",
				  notes = "The response format depends on the input type parameter: xml or json<br>" +
						  "Request URI sample: /ipservice/check?type=json<br>", 
				  response = String.class)
	@ApiResponses(value = {
	        @ApiResponse(code = 400, message = "Invalid input parameters"),
	        @ApiResponse(code = 500, message = "Internal server error")
	        })
	public Response checkRequesterLocation(@ApiParam(value = "Expected response format: xml or json - default: json", required = false) @QueryParam("output") @DefaultValue("json") String outputFormat) {
		return null;
	}

}
