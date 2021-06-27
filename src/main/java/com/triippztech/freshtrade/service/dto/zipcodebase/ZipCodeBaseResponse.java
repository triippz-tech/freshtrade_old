package com.triippztech.freshtrade.service.dto.zipcodebase;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "query", "results" })
public class ZipCodeBaseResponse {

    @JsonProperty("query")
    private Query query = new Query();

    @JsonProperty("results")
    private Results results = new Results();

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("query")
    public Query getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(Query query) {
        this.query = query;
    }

    @JsonProperty("results")
    public Results getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(Results results) {
        this.results = results;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public static ZipCodeBaseResponse generateErrorResponse(String zipCode, String country) {
        var zipCodeBaseResponse = new ZipCodeBaseResponse();
        zipCodeBaseResponse.getQuery().setCodes(Collections.singletonList(zipCode));
        zipCodeBaseResponse.getQuery().setCountry(country);
        return zipCodeBaseResponse;
    }
}
