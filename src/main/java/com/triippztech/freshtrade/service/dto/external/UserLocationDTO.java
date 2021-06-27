package com.triippztech.freshtrade.service.dto.external;

import java.io.Serializable;

public class UserLocationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String countryCode;
    private String countryName;
    private String city;
    private String postal;
    private String latitude;
    private String longitude;
    private String IP;
    private String state;
    private String conversionType;

    public UserLocationDTO() {}

    public UserLocationDTO(GeoLocationDBDTO geoLocationDB) {
        this.countryCode = geoLocationDB.getCountryCode();
        this.countryName = geoLocationDB.getCountryName();
        this.city = geoLocationDB.getCity();
        this.postal = geoLocationDB.getPostal();
        this.latitude = geoLocationDB.getLatitude().toString();
        this.longitude = geoLocationDB.getLongitude().toString();
        this.IP = geoLocationDB.getIPv4();
        this.state = geoLocationDB.getState();
        this.conversionType = geoLocationDB.getCountryCode().equals("US") ? "MI" : "KM";
    }

    public static UserLocationDTO defaultLoc() {
        UserLocationDTO userLoc = new UserLocationDTO();
        userLoc.countryCode = "US";
        userLoc.countryName = "United States";
        userLoc.city = "Lancaster";
        userLoc.postal = "17601";
        userLoc.latitude = "40.0721";
        userLoc.longitude = "76.3058";
        userLoc.IP = "127.0.0.1";
        userLoc.state = "PA";
        userLoc.conversionType = "MI";
        return userLoc;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConversionType() {
        return conversionType;
    }

    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }
}
