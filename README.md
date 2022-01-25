# jbossfuse-cxfrs-demo
_Red Hat JBoss Fuse 6.3.0_ projects to demo the usage of the camel CXFRS endpoints in order to expose or consume RESTful services running on _Apache Karaf_ containers (standalone or _Fabric8_-managed cluster)
* [ipservice\_cxfrs\_server\_swaggerv1](ipservice_cxfrs_server_swaggerv1) (`swagger v1.2`)
* [ipservice\_cxfrs\_server\_swaggerv2](ipservice_cxfrs_server_swaggerv2) (`swagger v2.0`)
* [oidcsecured\_ipservice\_cxfrs\_server\_swaggerv2](oidcsecured_ipservice_cxfrs_server_swaggerv2) (`swagger v2.0` and secured with OpenID Connect protocol)
* [ipservice\_cxfrs\_client](ipservice_cxfrs_client)

## :warning: WARNING:
- The *__Red Hat JBoss Fuse 6.3.0 Rollup 19 (v6.3.0.redhat-515) BOM__* is used in this project. So make sure you use the same patch version or
  adapt to your current patch version of _Red Hat JBoss Fuse 6.3.0_
- This project is configured to use a repository manager.
Thus, the [parent POM](pom.xml) points to my private [Sonatype Nexus Repository OSS](https://www.sonatype.com/download-oss-sonatype).
My [Sonatype Nexus Repository OSS](https://www.sonatype.com/download-oss-sonatype) is configured to proxy the following 
Red Hat maven repositories in addition to [Maven Central](https://repo1.maven.org/maven2):
  - https://maven.repository.redhat.com/ga 
  - https://maven.repository.redhat.com/earlyaccess/all
  - Make sure you configure the [parent POM](pom.xml) to either point to
your own maven repository manager or directly to [Maven Central](https://repo1.maven.org/maven2) and
the two Red Hat maven repositories above.
- Various PID properties need to be adjusted according to your environment:
  - either within the projects blueprint for local tests with `camel:run` maven goal
  - either within the [parent PID](src/main/fabric8/org.jeannyil.fuse.cxfrs.demo.properties) and the following PID files for container runtime (_Apache Karaf_ standalone or managed by _Fabric8_)
tests according to the tested module:
    - [ipservice\_cxfrs\_server\_swaggerv1 PID](ipservice_cxfrs_server_swaggerv1/src/main/fabric8/org.jeannyil.fuse.demo.ipservicecxfrsserver.properties)
    - [ipservice\_cxfrs\_server\_swaggerv2 PID](ipservice_cxfrs_server_swaggerv2/src/main/fabric8/org.jeannyil.fuse.demo.ipservicecxfrsserver.properties)
    - [oidcsecured\_ipservice\_cxfrs\_server\_swaggerv2 PID](oidcsecured_ipservice_cxfrs_server_swaggerv2/src/main/fabric8/org.jeannyil.fuse.demo.oidcsecured.ipservicecxfrsserver.properties)
    - [ipservice\_cxfrs\_client PID](ipservice_cxfrs_client/src/main/fabric8/org.jeannyil.fuse.demo.oidcsecured.ipservicecxfrsclient.properties)

## Build instructions

All four modules can be built from the parent project (_[jbossfuse-cxfrs-demo](../jbossfuse-cxfrs-demo)_).

Please follow the instructions below according to the target deployment type: standalone or in a _fabric8_-managed cluster

### Build for a standalone deployment

```
$ cd jbossfuse-cxfrs-demo
$ mvn clean install
```

### Build for a _fabric8_-managed cluster deployment

```
$ cd jbossfuse-cxfrs-demo
$ mvn clean package fabric8:zip install
```

Five _fabric8_ profiles are generated:
- `org-jeannyil-fuse-cxfrs-demo`: the _fabric8_ parent profile for all the modules
- `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client`: the [ipservice\_cxfrs\_client](ipservice_cxfrs_client) module _fabric8_ profile
- `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1`: the [ipservice\_cxfrs\_server\_swaggerv1](ipservice_cxfrs_server_swaggerv1) _fabric8_ profile
- `org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv2`: the [ipservice\_cxfrs\_server\_swaggerv2](ipservice_cxfrs_server_swaggerv2) _fabric8_ profile
- `org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2`: the [oidcsecured\_ipservice\_cxfrs\_server\_swaggerv2](oidcsecured_ipservice_cxfrs_server_swaggerv2) _fabric8_ profile

In order to import these profiles in a _Red Hat Fuse fabric8_-managed cluster:
- Log into the _Red Hat Fuse fabric8_ terminal and import the generated `org-jeannyil-fuse-cxfrs-demo` _fabric8_ parent profile:
  ```
  $ fabric:profile-import mvn:org.jeannyil.fuse/cxfrs-demo/1.0.0-SNAPSHOT/zip/profile
  ```
- All the four modules _fabric8_ profiles will be automatically imported:
  ```
  $ fabric:profile-list | grep jeannyil
    org-jeannyil-fuse-cxfrs-demo                                                   default feature-camel feature-camel-jms feature-cxf
    org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_client                            org-jeannyil-fuse-cxfrs-demo
    org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv1                  org-jeannyil-fuse-cxfrs-demo
    org-jeannyil-fuse-cxfrs-demo-ipservice_cxfrs_server_swaggerv2                  org-jeannyil-fuse-cxfrs-demo
    org-jeannyil-fuse-cxfrs-demo-oidcsecured_ipservice_cxfrs_server_swaggerv2      org-jeannyil-fuse-cxfrs-demo
  ```

## Deployment and test instructions

Deployment and test instructions for each module can be found in its _README_ accordingly:
- [ipservice\_cxfrs\_server\_swaggerv1](ipservice_cxfrs_server_swaggerv1/README.md)
- [ipservice\_cxfrs\_server\_swaggerv2](ipservice_cxfrs_server_swaggerv2/README.md)
- [oidcsecured\_ipservice\_cxfrs\_server\_swaggerv2](oidcsecured_ipservice_cxfrs_server_swaggerv2/README.md)
- [ipservice\_cxfrs\_client](ipservice_cxfrs_client/README.md)

## Screenshot samples

### Deployment on a standalone instance of _Red Hat JBoss Fuse 6.3.0 on Apache Karaf_ 
![Fuse on Apache Karaf Standalone Deployment](images/Fuse_Standalone_Deployment.png)

### Deployment in a _Fabric8_-managed  _Red Hat JBoss Fuse 6.3.0_ cluster environment
![Fuse on Apache Karaf Fabric Cluster Deployment](images/Fuse_Fabric8_Deployment.png)