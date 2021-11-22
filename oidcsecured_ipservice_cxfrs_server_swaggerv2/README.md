# Camel CXFRS Demo (swagger v2.0) :: OIDC-secured :: A Red Hat JBoss Fuse 6.3.0 RESTful service that handles operations on an IP address or a hostname

This project can be deployed both in a _Red Hat JBoss Fuse 6.3.0_ standalone _karaf_ container or in a _Fabric8_-managed cluster environment.

## Deployment on a standalone instance of _Red Hat Fuse 6.3.0/Apache Karaf_ 

### Assumptions
- _Red Hat JBoss Fuse 6.3.0 Rollup 18 (v6.3.0.redhat-495) on Apache Karaf_ is installed and running in standalone mode
- _Red Hat JBoss AMQ_ broker is either running inside in the _Red Hat JBoss Fuse 6.3.0 Rollup 18 (v6.3.0.redhat-495) on Apache Karaf_ (embedded) or
is running as a standalone instance
- A _Red Hat Single Sign-On 7.5_ or _Keycloak 15_ instance is installed and running
- The project has been [built for a standalone deployment](../README.md#build-for-a-standalone-deployment)

## Deployment in a standalone instance of _Red Hat Fuse 6.3.0 on Apache Karaf_ 

### Assumptions
- _Red Hat JBoss Fuse 6.3.0 Rollup 18 (v6.3.0.redhat-495) on Apache Karaf_ is installed and running in standalone mode
- _Red Hat JBoss AMQ_ broker is either running inside in the _Red Hat JBoss Fuse 6.3.0 Rollup 18 (v6.3.0.redhat-495) on Apache Karaf_ (embedded) or
is running as a standalone instance
- A _Red Hat Single Sign-On 7.5_ or _Keycloak 15_ instance is installed and running
- The project has been [built for a standalone deployment](../README.md#build-for-a-standalone-deployment)

### Deployment instructions

- Create the `org.jeannyil.fuse.demo.oidcsecured.ipservicecxfrsserver.cfg` _Persistent ID_ file in the `<red_hat_fuse_install_directory>/etc` directory with the following content:
  ```
  context.name.application=demo-oidcsecured_ipservice_cxfrs_server
  camel.name.route=demo-oidcsecured_ipservicecxfrsserver-route
  notif.amq.destination=queue://IPSERVICE.CXFRSSERVER.NOTIF.QUEUE
  error.amq.destination=queue://IPSERVICE.CXFRSSERVER.ERROR.QUEUE
  amqclient.ssl.truststore=/Users/jnyilimb/workdata/truststore/truststore.jks
  amqclient.ssl.truststore.password=secret
  broker.out.url=failover:(ssl://amq-standalone.lab.com:61443)?jms.useCompression=true
  broker.user.name=amq
  broker.user.password=amq@standalone
  broker.max.connections=1
  broker.max.activesessionperconnection=500
  output.message.ttl.inms=3600000
  exposed.service.gateway.host=fuse-standalone.lab.com
  exposed.service.gateway.port=8443
  http.client.connection.timeout.inms=60000
  http.client.receive.timeout.inms=120000
  keycloak.adapter.realm=fuse-fabric-demo
  keycloak.adapter.authServerUrl=https://sso.apps.cluster-phxmk.phxmk.sandbox911.opentlc.com/auth
  keycloak.adapter.sslRequired=ALL
  keycloak.adapter.verifyTokenAudience=true
  ```
- Adapt the following properties according to your run-time environment:
  - `amqclient.ssl.truststore`: path to the truststore containing the AMQ broker public certificate
  - `amqclient.ssl.truststore.password`: password of the truststore
  - `broker.out.url`: _Red Hat JBoss AMQ_ broker connection url (openwire)
  - `broker.user.name` and `broker.user.password`: credentials to connect to the _Red Hat JBoss AMQ_ broker
  - `exposed.service.gateway.host` and `exposed.service.gateway.port`: these are respectively the host and port where the
  RESTful service is exposed. They are used for the service dynamically-generated `swagger v2.0` specification.
  - JBoss Fuse 6.3 Keyclaok Adapter configuration properties (see [here](https://access.redhat.com/documentation/en-us/red_hat_single_sign-on/7.5/html/securing_applications_and_services_guide/oidc#java_adapter_config) for more details):
      - `keycloak.adapter.realm`: Name of the realm securing the application.
      - `keycloak.adapter.authServerUrl`: The base URL of the Red Hat Single Sign-On server. It is usually of the form `https://host:port/auth`
      - `keycloak.adapter.sslRequired`: Ensures that all communication to and from the Red Hat Single Sign-On server is over HTTPS. Valid values are 'all', 'external' and 'none'. 
      - `keycloak.adapter.useResourceRoleMappings`: If set to true, the adapter will look inside the token for application level role mappings for the user. If false, it will look at the realm level for user role mappings. The default value is false.
      - `keycloak.adapter.verifyTokenAudience`: If set to true, then during authentication with the bearer token, the adapter will verify whether the token contains this client name (resource) as an audience. The default value is false.
- Log into the _Red Hat Fuse Karaf_ terminal and deploy the `oidcsecured_ipservice_cxfrs_server_swaggerv2`feature:
  ```
  $ features:addurl mvn:org.jeannyil.fuse/oidcsecured_ipservice_cxfrs_server_swaggerv2/1.0.0-SNAPSHOT/xml/features
  $ features:install oidcsecured_ipservice_cxfrs_server_swaggerv2
  ``` 

## Deployment in a _fabric8_-managed cluster of _Red Hat Fuse 6.3.0 on Apache Karaf_ 

### Assumptions
- A _fabric8_-managed cluster of _Red Hat JBoss Fuse 6.3.0 Rollup 18 (v6.3.0.redhat-495)_ is running
- _Red Hat JBoss AMQ_ broker is either running within the _fabric8_-managed cluster of _Red Hat JBoss Fuse 6.3.0 Rollup 18 (v6.3.0.redhat-495)_ or
as a standalone instance outside the _fabric8_ cluster
- The project has been [built for a _Red Hat Fuse fabric8_-managed cluster deployment](../README.md#build-for-a-_fabric8_-managed-cluster-deployment)
- All the generated _fabric8_ profiles have been imported (See instructions [here](../README.md#build-for-a-_fabric8_-managed-cluster-deployment))

### Deployment instructions

- Log into the _Red Hat Fuse fabric8_ terminal
- Use the `fabric:profile-edit` command (example: `fabric:profile-edit -p org.jeannyil.fuse.cxfrs.demo/broker.user.name=amq org-jeannyil-fuse-cxfrs-demo`) 
to adapt some _Persistent ID_ properties as indicated for the following _fabric8_ profiles:
  - `org-jeannyil-fuse-cxfrs-demo` _fabric8_ profile
    - Adapt these `org.jeannyil.fuse.cxfrs.demo` _Persistent ID_ properties: 
      - `amqclient.ssl.truststore`: path to the truststore containing the AMQ broker public certificate
      - `amqclient.ssl.truststore.password`: password of the truststore
      - `broker.out.url`: _Red Hat JBoss AMQ_ broker connection url (openwire)
      - `broker.user.name` and `broker.user.password`: credentials to connect to the _Red Hat JBoss AMQ_ broker
    - Below is an extract of my `org-jeannyil-fuse-cxfrs-demo` _fabric8_ profile:
      ```
      $ fabric:profile-display org-jeannyil-fuse-cxfrs-demo
      Profile id: org-jeannyil-fuse-cxfrs-demo
      [...]
      Configuration details
      ----------------------------
      PID: org.jeannyil.fuse.cxfrs.demo
        broker.out.url discovery:(fabric://ssl-demo-broker)
        amqclient.ssl.truststore /Users/jnyilimb/workdata/truststore/truststore.jks
        broker.max.activesessionperconnection 500
        broker.max.connections 1
        broker.user.name amq
        broker.user.password ${crypt:AXwoRz+nwJEtZtayqOprP8VFTxMJl5EZ}
        amqclient.ssl.truststore.password ${crypt:/ynd1l0QTCcuaeJdcdAsrA==}
        output.message.ttl.inms 3600000
      [...]
      ```
  - `org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2` _fabric8_ profile
    - Adapt these `org.jeannyil.fuse.demo.ipservicecxfrsserver` _Persistent ID_ properties:
      - `exposed.service.gateway.host` and `exposed.service.gateway.port`: these are respectively the host and port where the
      RESTful service is exposed. They are used for the service dynamically-generated `swagger v2.0` specification.
      - JBoss Fuse 6.3 Keyclaok Adapter configuration properties (see [here](https://access.redhat.com/documentation/en-us/red_hat_single_sign-on/7.5/html/securing_applications_and_services_guide/oidc#java_adapter_config) for more details):
        - `keycloak.adapter.realm`: Name of the realm securing the application.
        - `keycloak.adapter.authServerUrl`: The base URL of the Red Hat Single Sign-On server. It is usually of the form `https://host:port/auth`
        - `keycloak.adapter.sslRequired`: Ensures that all communication to and from the Red Hat Single Sign-On server is over HTTPS. Valid values are 'all', 'external' and 'none'. 
        - `keycloak.adapter.useResourceRoleMappings`: If set to true, the adapter will look inside the token for application level role mappings for the user. If false, it will look at the realm level for user role mappings. The default value is false.
        - `keycloak.adapter.verifyTokenAudience`: If set to true, then during authentication with the bearer token, the adapter will verify whether the token contains this client name (resource) as an audience. The default value is false.
    - Below is an extract of the `org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2` _fabric8_ profile:
      ```
      $ fabric:profile-display org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2
      Profile id: org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2
      [...]         
      Configuration details
      ----------------------------
      PID: org.jeannyil.fuse.demo.oidcsecured.ipservicecxfrsserver
        error.amq.destination queue://IPSERVICE.CXFRSSERVER.ERROR.QUEUE
        broker.out.url ${profile:org.jeannyil.fuse.cxfrs.demo/broker.out.url}
        broker.max.activesessionperconnection ${profile:org.jeannyil.fuse.cxfrs.demo/broker.max.activesessionperconnection}
        broker.max.connections ${profile:org.jeannyil.fuse.cxfrs.demo/broker.max.connections}
        http.client.receive.timeout.inms 120000
        keycloak.adapter.authServerUrl https://sso.apps.cluster-phxmk.phxmk.sandbox911.opentlc.com/auth
        keycloak.adapter.verifyTokenAudience true
        keycloak.adapter.sslRequired ALL
        notif.amq.destination queue://IPSERVICE.CXFRSSERVER.NOTIF.QUEUE
        broker.user.password ${profile:org.jeannyil.fuse.cxfrs.demo/broker.user.password}
        amqclient.ssl.truststore.password ${profile:org.jeannyil.fuse.cxfrs.demo/amqclient.ssl.truststore.password}
        exposed.service.gateway.host localhost
        amqclient.ssl.truststore ${profile:org.jeannyil.fuse.cxfrs.demo/amqclient.ssl.truststore}
        context.name.application demo-oidcsecured_ipservice_cxfrs_server
        broker.user.name ${profile:org.jeannyil.fuse.cxfrs.demo/broker.user.name}
        http.client.connection.timeout.inms 60000
        exposed.service.gateway.port 9095
        camel.name.route demo-oidcsecured_ipservicecxfrsserver-route
        keycloak.adapter.realm fuse-fabric-demo
        output.message.ttl.inms ${profile:org.jeannyil.fuse.cxfrs.demo/output.message.ttl.inms}
        keycloak.adapter.useResourceRoleMappings true
      [...]
      ```
- Deploy the `org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2`_fabric8_ profile:
  - On an existing _fabric8 karaf_ container using the `fabric:container-add-profile` command.
  Example: 
    ```
    $ fabric:container-add-profile cxfrs_server-node org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2
    ```
  - Or, create a new _fabric8 karaf_ container and assign the profile using the `fabric:container-create-child` command.
  Example:
    ```
    $ fabric:container-create-child --profile org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2 fuse-01-root cxfrs_server-node
    ```

:construction: *__README TO BE COMPLETED__ with tests samples for both standalone and fabric8 deployments...*
