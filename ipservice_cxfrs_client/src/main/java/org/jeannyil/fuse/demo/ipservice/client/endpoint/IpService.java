package org.jeannyil.fuse.demo.ipservice.client.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(value="/ipservice")
public interface IpService {
	@GET
	@Path("/geolocation")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String getGeoLocation(@MatrixParam("type") String type, @MatrixParam("ip") String ip);
}
