package com.triippztech.freshtrade.service.dto.zipcodebase;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Results {

    private Map<String, List<ZipCode>> zipCodes = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, List<ZipCode>> getZipCodes() {
        return this.zipCodes;
    }

    @JsonAnySetter
    public void setZipCode(String name, List<ZipCode> zipCodes) {
        this.zipCodes.put(name, zipCodes);
    }
}
