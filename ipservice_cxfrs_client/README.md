# Camel CXFRS Demo :: A Red Hat JBoss Fuse 6.3.0 RESTful client for the IPSERVICE CXFRS Server exposed behind an SSL-enabled fabric8 gateway
- This project can be deployed both in a Red Hat JBoss Fuse 6.3.0 standalone karaf container or in a Fabric8 environment. 
The features repository for a standalone deployment in a karaf container works fine.

- The request parameters to call the **_[IpService CXFRS server](../ipservice_cxfrs_server)_** 
are fetched from a JSON file. Below is an example in the JSON format:
```json
{
  "parameters": [
    {
      "ip_or_hostname": "",
      "response_type": "xml"
    },
    {
      "ip_or_hostname": "redhat.com",
      "response_type": "json"
    },
    {
      "ip_or_hostname": "org.example",
      "response_type": "csv"
    },
    {
      "ip_or_hostname": "ipstack.com",
      "response_type": "json"
    }
  ]
}
```

## :warning: ATTENTION:
- Currently, the **_[IpService CXFRS server](../ipservice_cxfrs_server)_** 
will be called **only if** the input JSON file is valid according to the _[inputMessageSchema.json](src/main/resources/Schemas/inputMessageSchema.json)_.
The **_[Everit JSON validator library](https://github.com/everit-org/json-schema)_** is used for JSON validation.

## Deployment on a standalone instance of _Red Hat Fuse 6.3.0/Apache Karaf_ 

### Assumptions
- _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329) on Apache Karaf_ is installed and running in standalone mode
- _Red Hat JBoss AMQ_ broker is either running inside in the _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329) on Apache Karaf_ (embedded) or
is running as a standalone instance
- The project has been [built for a standalone deployment](../README.md#build-for-a-standalone-deployment)

### Deployment instructions

- Create the `org.jeannyil.fuse.demo.ipservicecxfrsclient.cfg` _Persistent ID_ file in the *__<red_hat_fuse_install_directory>/etc__* with the 
following content. :warning: You have to adapt the following properties according to your run-time environment:
  - `amqclient.ssl.truststore`: path to the truststore containing the AMQ broker public certificate
  - `amqclient.ssl.truststore.password`: password of the truststore
  - `broker.out.url`: _Red Hat JBoss AMQ_ broker connection url (openwire)
  - `broker.user.name` and `broker.user.password`: credentials to connect to the _Red Hat JBoss AMQ_ broker
  - `json.file.input.path` and `json.file.error.path`: working directories (respectively input and error) for the `ipservice_cxfrs_client` bundle 
  - `restful.service.base.url`: base URL of the the **[ipservice_cxfrs_server](../ipservice_cxfrs_server_swaggerv2)** RESTful service
  - `restful.service.ssl.truststore`: path to the truststore containing the **[ipservice_cxfrs_server](../ipservice_cxfrs_server_swaggerv2)** RESTful service public certificate
  - `restful.service.ssl.truststore.password`: password of the truststore
```
context.name.application=demo-ipservice_cxfrs_client
camel.name.route=demo-ipservice_cxfrs_client-route
notif.amq.destination=queue://IPSERVICE.CXFRSCLIENT.NOTIF.QUEUE
error.amq.destination=queue://IPSERVICE.CXFRSCLIENT.ERROR.QUEUE
amqclient.ssl.truststore=/home/jEAN/mnt/nfsshare/security/fuse-standalone.lab.com/jboss-fuse/app-truststore.jks
amqclient.ssl.truststore.password=P@ssw0rd
broker.out.url=failover:(ssl://amq-standalone.lab.com:61443)
broker.user.name=amq
broker.user.password=amq@standalone
broker.max.connections=1
broker.max.activesessionperconnection=500
http.client.connection.timeout.inms=60000
http.client.receive.timeout.inms=120000
output.message.ttl.inms=3600000
json.file.input.path=/home/jEAN/mnt/nfsshare/working/fuse-standalone.lab.com/workingDir/in
json.file.error.path=/home/jEAN/mnt/nfsshare/working/fuse-standalone.lab.com/workingDir/error
restful.service.base.url=https://fuse-standalone.lab.com:8443/cxf/demorest
restful.service.ssl.truststore=/home/jEAN/mnt/nfsshare/security/fuse-standalone.lab.com/jboss-fuse/app-truststore.jks
restful.service.ssl.truststore.password=P@ssw0rd
```
- Log into the _Red Hat Fuse Karaf_ terminal and deploy the `ipservice_cxfrs_client`feature:
```
$ features:addurl mvn:org.jeannyil.fuse/ipservice_cxfrs_client/1.0.0-SNAPSHOT/xml/features
$ features:install ipservice_cxfrs_client
``` 

## Deployment in a _fabric8_ cluster of _Red Hat Fuse 6.3.0/Apache Karaf_ 

### Assumptions
- A _fabric8_-managed cluster of _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329)_ is running
- _Red Hat JBoss AMQ_ broker is either running within the _fabric8_-managed cluster of _Red Hat JBoss Fuse 6.3.0 Rollup 6 (v6.3.0.redhat-329)_ or
as a standalone instance outside the _fabric8_ cluster
- The project has been [built for a _Red Hat Fuse fabric8_-managed cluster deployment](../README.md#build-for-a-_fabric8_-managed-cluster-deployment)
- All the generated _fabric8_ profiles have been imported (See instructions [here](../README.md#build-for-a-_fabric8_-managed-cluster-deployment))

### Deployment instructions

- Log into the _Red Hat Fuse Karaf_ terminal
- Use the `fabric:profile-edit` command to adapt _Persistent ID_ properties for the following _fabric8_ profiles:
  - `org.jeannyil.fuse.cxfrs.demo` _Persistent ID_ in the `org-jeannyil-fuse-cxfrs-demo` _fabric8_ profile.
  Example: `fabric:profile-edit -p org.jeannyil.fuse.cxfrs.demo/broker.user.name=amq org-jeannyil-fuse-cxfrs-demo`
    - `amqclient.ssl.truststore`: path to the truststore containing the AMQ broker public certificate
    - `amqclient.ssl.truststore.password`: password of the truststore
    - `broker.out.url`: _Red Hat JBoss AMQ_ broker connection url (openwire)
    - `broker.user.name` and `broker.user.password`: credentials to connect to the _Red Hat JBoss AMQ_ broker
  - `org.jeannyil.fuse.demo.ipservicecxfrsclient` _Persistent ID_ in the `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client` _fabric8_ profile.
  Example: `fabric:profile-edit -p org.jeannyil.fuse.demo.ipservicecxfrsclient/restful.service.base.url=https://fuse-01.lab.com:9095/version/1.0/cxf/demorest org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client`
    - `json.file.input.path` and `json.file.error.path`: working directories (respectively input and error) for the `ipservice_cxfrs_client` bundle 
    - `restful.service.base.url`: base URL of the the **[ipservice_cxfrs_server](../ipservice_cxfrs_server_swaggerv2)** RESTful service
    - `restful.service.ssl.truststore`: path to the truststore containing the **[ipservice_cxfrs_server](../ipservice_cxfrs_server_swaggerv2)** RESTful service public certificate
    - `restful.service.ssl.truststore.password`: password of the truststore
- Below are extracts of my `org-jeannyil-fuse-cxfrs-demo` and `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client` _fabric8_ profiles:
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

$ fabric:profile-display org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client
Profile id: org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client
[...]
Configuration details
----------------------------
PID: org.jeannyil.fuse.demo.ipservicecxfrsclient
  error.amq.destination queue://IPSERVICE.CXFRSCLIENT.ERROR.QUEUE
  broker.out.url ${profile:org.jeannyil.fuse.cxfrs.demo/broker.out.url}
  restful.service.ssl.truststore /home/jEAN/mnt/nfsshare/security/jboss-fuse/truststore.jks
  broker.max.activesessionperconnection ${profile:org.jeannyil.fuse.cxfrs.demo/broker.max.activesessionperconnection}
  broker.max.connections ${profile:org.jeannyil.fuse.cxfrs.demo/broker.max.connections}
  restful.service.ssl.truststore.password P@ssw0rd
  http.client.receive.timeout.inms 120000
  restful.service.base.url https://fuse-01.lab.com:9095/version/1.0/cxf/demorest
  notif.amq.destination queue://IPSERVICE.CXFRSCLIENT.NOTIF.QUEUE
  broker.user.password ${profile:org.jeannyil.fuse.cxfrs.demo/broker.user.password}
  amqclient.ssl.truststore.password ${profile:org.jeannyil.fuse.cxfrs.demo/amqclient.ssl.truststore.password}
  amqclient.ssl.truststore ${profile:org.jeannyil.fuse.cxfrs.demo/amqclient.ssl.truststore}
  context.name.application demo-ipservice_cxfrs_client
  broker.user.name ${profile:org.jeannyil.fuse.cxfrs.demo/broker.user.name}
  http.client.connection.timeout.inms 60000
  json.file.input.path /home/jEAN/mnt/nfsshare/working/fuse-fabric8.lab.com/workingDir/in
  json.file.error.path /home/jEAN/mnt/nfsshare/working/fuse-fabric8.lab.com/workingDir/error
  camel.name.route demo-ipservice_cxfrs_client-route
  output.message.ttl.inms ${profile:org.jeannyil.fuse.cxfrs.demo/output.message.ttl.inms}
[...]
```
- Deploy the `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client`_fabric8_ profile:
  - On an existing _fabric8 karaf_ container using the `fabric:container-add-profile` command.
  Example: 
  ```
  $ fabric:container-add-profile cxfrs_client-node org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client
  ```
  - Or, create a new _fabric8 karaf_ container and assign the profile using the `fabric:container-create-child` command.
  Example:
  ```
  $ fabric:container-create-child --profile org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client fuse-01-root cxfrs_client-node
  ```

:construction: *__README TO BE COMPLETED__ with tests samples for both standalone and fabric8 deployments...*
