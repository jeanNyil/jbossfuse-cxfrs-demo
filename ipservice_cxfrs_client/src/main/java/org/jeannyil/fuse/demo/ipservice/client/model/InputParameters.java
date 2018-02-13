package org.jeannyil.fuse.demo.ipservice.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "parameters"
})
public class InputParameters {

    @JsonProperty("parameters")
    List<Parameters> parameters = new ArrayList<Parameters>();

    @JsonProperty("parameters")
    public List<Parameters> getParameters() {
        return parameters;
    }

    @JsonProperty("parameters")
    public void setParameters(List<Parameters> parameters) {
        this.parameters = parameters;
    }
}
