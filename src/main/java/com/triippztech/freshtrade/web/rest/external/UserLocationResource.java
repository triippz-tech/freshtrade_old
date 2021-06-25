package com.triippztech.freshtrade.web.rest.external;

import com.triippztech.freshtrade.service.dto.external.GeoLocationDBDTO;
import com.triippztech.freshtrade.service.dto.external.UserLocationDTO;
import com.triippztech.freshtrade.service.dto.zipcodebase.ZipCodeBaseResponse;
import com.triippztech.freshtrade.service.external.GeoLocationDBService;
import com.triippztech.freshtrade.service.external.ZipCodeBaseService;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserLocationResource {

    private final Logger log = LoggerFactory.getLogger(UserLocationResource.class);

    private final ZipCodeBaseService zipCodeBaseService;

    private final GeoLocationDBService geoLocationDBService;

    public UserLocationResource(ZipCodeBaseService zipCodeBaseService, GeoLocationDBService geoLocationDBService) {
        this.zipCodeBaseService = zipCodeBaseService;
        this.geoLocationDBService = geoLocationDBService;
    }

    @GetMapping("/user-location/postal-code")
    @ResponseBody
    public ResponseEntity<ZipCodeBaseResponse> searchZipCode(@RequestParam String code, @RequestParam String country) {
        log.debug("Request to search for Postal Code: {} in Country: {}", code, country);
        return ResponseEntity.status(200).body(zipCodeBaseService.searchZipCode(code, country));
    }

    @GetMapping("/user-location/ip")
    @ResponseBody
    public ResponseEntity<UserLocationDTO> getLocationByIP(HttpServletRequest request) {
        log.debug("Request to search for location by IP");
        return ResponseEntity.status(200).body(geoLocationDBService.getLocationFromIp(request));
    }
}
