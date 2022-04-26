package com.zooplus.crypto.services;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.zooplus.crypto.models.CryptoApi;
import com.zooplus.crypto.models.FlatMap;

import java.io.IOException;
import java.util.List;

/**
 * @author mhmdz
 * Created By Zeeshan on 26-04-2022
 * @project zooplus-crypto
 */
public interface CrytoApiService {

    String getListOfCrytoCurrencies();
    CryptoApi getSelectedCrypto(String symbol, String ipAddress) throws IOException, GeoIp2Exception;
    List<FlatMap> saveFlatMap();

}
