package org.jeannyil.fuse.demo.ipservice.client.endpoint;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path(value="/ipservice")
public interface IpService {
	@GET
	@Path(value="/geolocation")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String getGeoLocation(@QueryParam("ip") String ip, @QueryParam("output") String output);
}
