package com.zooplus.crypto.controllers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zooplus.crypto.models.CryptoApi;
import com.zooplus.crypto.models.FlatMap;
import com.zooplus.crypto.models.GeoIP;
import com.zooplus.crypto.services.CrytoApiService;
import com.zooplus.crypto.services.RawDBDemoGeoIPLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mhmdz
 * Created By Zeeshan on 26-04-2022
 * @project zooplus-crypto
 */

@RestController
public class CryptoApiController {

    @Autowired
    CrytoApiService crytoApiService;


    private RawDBDemoGeoIPLocationService locationService;

    public void CrytoApiController() throws IOException, IOException {
        locationService = new RawDBDemoGeoIPLocationService();
    }

    @GetMapping(value = "/cryptoCurrencies")
    public ResponseEntity getAllCrytoList() {
        try {
            String listAsString = crytoApiService.getListOfCrytoCurrencies();
            JsonObject jsonObject = JsonParser.parseString(listAsString).getAsJsonObject();
            jsonObject.get("data").getAsJsonArray();
            List<CryptoApi> cryptoApiList = new ArrayList<>();
            CryptoApi cryptoApi = null;
            Double price = null;

            for (JsonElement j : jsonObject.get("data").getAsJsonArray()){
                cryptoApi = new CryptoApi();
                cryptoApi.setCryptoCurrencyName(j.getAsJsonObject().get("name").getAsString());
                cryptoApi.setSymbol(j.getAsJsonObject().get("symbol").getAsString());
                price = j.getAsJsonObject().get("quote").getAsJsonObject().get("USD").getAsJsonObject().get("price").getAsDouble();
                cryptoApi.setPrice(price);
                cryptoApiList.add(cryptoApi);
            }
            return ResponseEntity.ok(cryptoApiList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/GeoIPTest")
    public GeoIP getLocation(@RequestParam(value="ipAddress", required=true) String ipAddress) throws Exception {


        RawDBDemoGeoIPLocationService locationService = new RawDBDemoGeoIPLocationService();
        return locationService.getLocation(ipAddress);
    }

    @GetMapping("/getFlatmap")
    public ResponseEntity getFlatMaps(){
        try{
            List<FlatMap> flatMaps = crytoApiService.saveFlatMap();
            return ResponseEntity.ok(flatMaps);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getSelectedCrypto")
    public ResponseEntity getSelectedCrypto(@RequestParam String symbol, @RequestParam(required = false) String ipAddress){
        try{
            CryptoApi cryptoApi = crytoApiService.getSelectedCrypto(symbol, ipAddress);
            return ResponseEntity.ok(cryptoApi);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
