# Camel CXFRS Demo :: A Red Hat JBoss Fuse 6.3.0 RESTful client for the IPSERVICE CXFRS Server exposed behind an SSL-enabled fabric8 gateway
- This project can be deployed both in a Red Hat JBoss Fuse 6.3.0 standalone karaf container or in a Fabric8 environment. 
The features repository for a standalone deployment in a karaf container works fine. \
**TO BE COMPLETED:** *Fabric8 environment deployment* 

- The input parameters for the IpService CXFRS server are fetched from a JSON file.

**ATTENTION:** 
- *The* **camel:run** *goal of the Camel maven plugin does not work when the http-conduit is activated in the blueprint*.
- *Everything works fine when deployed in a standalone RH JBoss Fuse container (karaf)...* 

**TODO:** 
- Resolve *camel:run* maven goal issue when the http-conduit is activated on the CXF rsClient endpoint.
- *README TO BE COMPLETED with deployment and run instructions...*
