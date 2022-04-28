package com.zooplus.crypto.services;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import com.zooplus.crypto.models.GeoIP;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @author mhmdz
 * Created By Zeeshan on 26-04-2022
 * @project zooplus-crypto
 */


public class RawDBDemoGeoIPLocationService {

    private DatabaseReader dbReader;

    //Geolite Db configuration for Country
    public RawDBDemoGeoIPLocationService() throws IOException {
        File database = new File("src/GeoLite2-Country.mmdb");
        dbReader = new DatabaseReader.Builder(database).build();
    }

    //this will return IP Address, ISO Code of country like "US" for USA and Name of the country
    public GeoIP getLocation(String ip) throws IOException, GeoIp2Exception {
        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse response = dbReader.country(ipAddress);
        Country country = response.getCountry();
        String countryName = country.getName();
        String isoCode = country.getIsoCode();
        return new GeoIP(ip, isoCode, countryName);
    }

}
