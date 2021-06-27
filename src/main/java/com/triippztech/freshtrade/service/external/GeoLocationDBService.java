package com.triippztech.freshtrade.service.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.triippztech.freshtrade.service.dto.external.GeoLocationDBDTO;
import com.triippztech.freshtrade.service.dto.external.UserLocationDTO;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationDBService {

    private final Logger log = LoggerFactory.getLogger(GeoLocationDBService.class);
    private final String LOCALHOST_IPV4 = "127.0.0.1";
    private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    private final RestTemplate restTemplate;

    @Value("${application.geolocation-db-token}")
    private String geolocationDbToken;

    public GeoLocationDBService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserLocationDTO getLocationFromIp(HttpServletRequest request) {
        var ip = getClientIp(request);
        var url = String.format("https://geolocation-db.com/json/%s/%s", geolocationDbToken, ip);

        var objectMapper = new ObjectMapper();
        try {
            var geoLocation = objectMapper.readValue(restTemplate.getForObject(url, String.class), GeoLocationDBDTO.class);
            if (geoLocation.getCity().equalsIgnoreCase("Not found")) return UserLocationDTO.defaultLoc();
            return new UserLocationDTO(geoLocation);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            return UserLocationDTO.defaultLoc();
        }
    }

    public String getClientIpAddr(HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        if (ip.equals("0:0:0:0:0:0:0:1")) return "";
        return ip;
    }

    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (LOCALHOST_IPV4.equals(ipAddress) || LOCALHOST_IPV6.equals(ipAddress)) {
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        return ipAddress;
    }
}
