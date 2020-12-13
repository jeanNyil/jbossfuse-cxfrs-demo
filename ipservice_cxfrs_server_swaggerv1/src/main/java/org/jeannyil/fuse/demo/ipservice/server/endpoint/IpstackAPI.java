package org.jeannyil.fuse.demo.ipservice.server.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * ipstack - Free IP Geolocation API documentation: https://ipstack.com/documentation
 */
@Path(value="/")
public interface IpstackAPI {
 
    @GET
    @Path(value="/{ip}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String getGeoIp(@PathParam("ip") String ip,
                           @QueryParam("access_key") String accessKey,
                           @QueryParam("output") String outputFormat);

    @GET
    @Path(value="/check")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String getRequesterLocation(@QueryParam("access_key") String accessKey,
                           @QueryParam("output") String outputFormat);
 
}

