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
will be called **only if** the input JSON file is valid according to the 
embedded _[inputMessageSchema.json](src/main/resources/Schemas/inputMessageSchema.json)_.
The **_[Everit JSON validator library](https://github.com/everit-org/json-schema)_** is used.

## :construction: TODO: 
- *__README TO BE COMPLETED__ with deployment and run instructions...*
