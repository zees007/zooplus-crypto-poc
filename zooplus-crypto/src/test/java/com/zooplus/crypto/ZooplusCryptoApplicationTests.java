package com.zooplus.crypto;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

@SpringBootTest
class ZooplusCryptoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void givenIP_whenFetchingCountry_thenReturnsCountryData()
            throws IOException, GeoIp2Exception {
        String ip = "101.53.159.255";
        String dbLocation = "src/GeoLite2-Country.mmdb";

        File database = new File(dbLocation);
        DatabaseReader dbReader = new DatabaseReader.Builder(database)
                .build();

        InetAddress ipAddress = InetAddress.getByName(ip);
        CountryResponse response = dbReader.country(ipAddress);
        Country country = response.getCountry();
        String countryName = country.getName();
        String isoCode = country.getIsoCode();

    }

}
