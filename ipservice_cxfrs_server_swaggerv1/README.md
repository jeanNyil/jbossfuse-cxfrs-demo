# Camel CXFRS Demo (swagger v1.2) :: A Red Hat JBoss Fuse 6.3.0 RESTful service that handles operations on an IP address or a hostname

This project can be deployed both in a _Red Hat JBoss Fuse 6.3.0_ standalone _karaf_ container or in a _Fabric8_-managed cluster environment.

## Deployment on a standalone instance of _Red Hat Fuse 6.3.0/Apache Karaf_ 

### Assumptions
- _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329) on Apache Karaf_ is installed and running in standalone mode
- _Red Hat JBoss AMQ_ broker is either running inside in the _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329) on Apache Karaf_ (embedded) or
is running as a standalone instance
- The project has been [built for a standalone deployment](../README.md#build-for-a-standalone-deployment)

## Deployment in a standalone instance of _Red Hat Fuse 6.3.0 on Apache Karaf_ 

### Assumptions
- _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329) on Apache Karaf_ is installed and running in standalone mode
- _Red Hat JBoss AMQ_ broker is either running inside in the _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329) on Apache Karaf_ (embedded) or
is running as a standalone instance
- The project has been [built for a standalone deployment](../README.md#build-for-a-standalone-deployment)

### Deployment instructions

- Create the `org.jeannyil.fuse.demo.ipservicecxfrsserver.cfg` _Persistent ID_ file in the `<red_hat_fuse_install_directory>/etc` directory with the 
following content:
```
context.name.application=demo-ipservice_cxfrs_server
camel.name.route=demo-ipservicecxfrsserver-route
notif.amq.destination=queue://IPSERVICE.CXFRSSERVER.NOTIF.QUEUE
error.amq.destination=queue://IPSERVICE.CXFRSSERVER.ERROR.QUEUE
amqclient.ssl.truststore=/home/jEAN/mnt/nfsshare/security/fuse-standalone.lab.com/jboss-fuse/app-truststore.jks
amqclient.ssl.truststore.password=P@ssw0rd
broker.out.url=failover:(ssl://amq-standalone.lab.com:61443)?jms.useCompression=true
broker.user.name=amq
broker.user.password=amq@standalone
broker.max.connections=1
broker.max.activesessionperconnection=500
output.message.ttl.inms=3600000
http.client.connection.timeout.inms=60000
http.client.receive.timeout.inms=120000
```
- Adapt the following properties according to your run-time environment:
  - `amqclient.ssl.truststore`: path to the truststore containing the AMQ broker public certificate
  - `amqclient.ssl.truststore.password`: password of the truststore
  - `broker.out.url`: _Red Hat JBoss AMQ_ broker connection url (openwire)
  - `broker.user.name` and `broker.user.password`: credentials to connect to the _Red Hat JBoss AMQ_ broker
- Log into the _Red Hat Fuse Karaf_ terminal and deploy the `ipservice_cxfrs_server_swaggerv1`feature:
```
$ features:addurl mvn:org.jeannyil.fuse/ipservice_cxfrs_server_swaggerv1/1.0.0-SNAPSHOT/xml/features
$ features:install ipservice_cxfrs_server_swaggerv1
``` 

## Deployment in a _fabric8_-managed cluster of _Red Hat Fuse 6.3.0 on Apache Karaf_ 

### Assumptions
- A _fabric8_-managed cluster of _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329)_ is running
- _Red Hat JBoss AMQ_ broker is either running within the _fabric8_-managed cluster of _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329)_ or
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
      amqclient.ssl.truststore /home/jEAN/mnt/nfsshare/security/jboss-fuse/truststore.jks
      broker.max.activesessionperconnection 500
      broker.max.connections 1
      broker.user.name amq
      broker.user.password amq@fabric
      amqclient.ssl.truststore.password P@ssw0rd
      output.message.ttl.inms 3600000
    [...]
    ```
  - `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1` _fabric8_ profile
    - You do not have to modify the `org.jeannyil.fuse.demo.ipservicecxfrsserver` _Persistent ID_ properties 
    - Below is an extract of the `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1` _fabric8_ profile:
    ```
    $ fabric:profile-display org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1
    Profile id: org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1
    [...]
    Configuration details
    ----------------------------
    PID: org.jeannyil.fuse.demo.ipservicecxfrsserver
      error.amq.destination queue://IPSERVICE.CXFRSSERVER.ERROR.QUEUE
      broker.out.url ${profile:org.jeannyil.fuse.cxfrs.demo/broker.out.url}
      broker.max.activesessionperconnection ${profile:org.jeannyil.fuse.cxfrs.demo/broker.max.activesessionperconnection}
      http.client.receive.timeout.inms 120000
      broker.max.connections ${profile:org.jeannyil.fuse.cxfrs.demo/broker.max.connections}
      notif.amq.destination queue://IPSERVICE.CXFRSSERVER.NOTIF.QUEUE
      broker.user.password ${profile:org.jeannyil.fuse.cxfrs.demo/broker.user.password}
      amqclient.ssl.truststore.password ${profile:org.jeannyil.fuse.cxfrs.demo/amqclient.ssl.truststore.password}
      amqclient.ssl.truststore ${profile:org.jeannyil.fuse.cxfrs.demo/amqclient.ssl.truststore}
      context.name.application demo-ipservice_cxfrs_server
      broker.user.name ${profile:org.jeannyil.fuse.cxfrs.demo/broker.user.name}
      http.client.connection.timeout.inms 60000
      camel.name.route demo-ipservicecxfrsserver-route
      output.message.ttl.inms ${profile:org.jeannyil.fuse.cxfrs.demo/output.message.ttl.inms}
    [...]
    ```
- Deploy the `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1`_fabric8_ profile:
  - On an existing _fabric8 karaf_ container using the `fabric:container-add-profile` command.
  Example: 
  ```
  $ fabric:container-add-profile cxfrs_server-node org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1
  ```
  - Or, create a new _fabric8 karaf_ container and assign the profile using the `fabric:container-create-child` command.
  Example:
  ```
  $ fabric:container-create-child --profile org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1 fuse-01-root cxfrs_server-node
  ```

:construction: *__README TO BE COMPLETED__ with tests samples for both standalone and fabric8 deployments...*
