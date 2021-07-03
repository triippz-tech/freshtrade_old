package com.triippztech.freshtrade.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Freshtrade.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String geolocationDbToken;
    private String zipcodeBaseToken;

    public String getGeolocationDbToken() {
        return geolocationDbToken;
    }

    public void setGeolocationDbToken(String geolocationDbToken) {
        this.geolocationDbToken = geolocationDbToken;
    }

    public String getZipcodeBaseToken() {
        return zipcodeBaseToken;
    }

    public void setZipcodeBaseToken(String zipcodeBaseToken) {
        this.zipcodeBaseToken = zipcodeBaseToken;
    }
}
