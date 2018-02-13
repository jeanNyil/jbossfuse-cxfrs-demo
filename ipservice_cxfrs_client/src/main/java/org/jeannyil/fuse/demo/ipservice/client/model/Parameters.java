package org.jeannyil.fuse.demo.ipservice.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ip_or_hostname",
        "response_type"
})
public class Parameters {

    @JsonProperty("ip_or_hostname")
    String ipOrHostname;
    @JsonProperty("response_type")
    String responseType;

    @JsonProperty("ip_or_hostname")
    public String getIpOrHostname() {
        return ipOrHostname;
    }

    @JsonProperty("ip_or_hostname")
    public void setIpOrHostname(String ipOrHostname) {
        this.ipOrHostname = ipOrHostname;
    }

    @JsonProperty("response_type")
    public String getResponseType() {
        return responseType;
    }

    @JsonProperty("response_type")
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}
