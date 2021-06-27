package com.triippztech.freshtrade.service.external;

import com.triippztech.freshtrade.service.dto.zipcodebase.Query;
import com.triippztech.freshtrade.service.dto.zipcodebase.ZipCodeBaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ZipCodeBaseService {

    private final Logger log = LoggerFactory.getLogger(ZipCodeBaseService.class);

    private final RestTemplate restTemplate;

    @Value("${application.zipcode-base-token}")
    private String zipCodeBaseToken;

    public ZipCodeBaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ZipCodeBaseResponse searchZipCode(String zipCode, String country) {
        try {
            return restTemplate.getForObject(
                String.format(
                    "https://app.zipcodebase.com/api/v1/search?apikey=%s&codes=%s&country=%s",
                    zipCodeBaseToken,
                    zipCode,
                    country
                ),
                ZipCodeBaseResponse.class
            );
        } catch (org.springframework.web.client.RestClientException ex) {
            log.warn("Could not find ZipCode: {} in Country: {}", zipCode, country);
            return ZipCodeBaseResponse.generateErrorResponse(zipCode, country);
        }
    }
}
