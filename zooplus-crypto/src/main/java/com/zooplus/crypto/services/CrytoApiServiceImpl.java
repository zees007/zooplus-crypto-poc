package com.zooplus.crypto.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.zooplus.crypto.models.CryptoApi;
import com.zooplus.crypto.models.FlatMap;
import com.zooplus.crypto.models.GeoIP;
import com.zooplus.crypto.repositories.FlatMapRepository;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * @author mhmdz
 * Created By Zeeshan on 26-04-2022
 * @project zooplus-crypto
 */

@Service
public class CrytoApiServiceImpl implements CrytoApiService {

    private static String apiKey = "74d4bed0-6436-48d2-8993-91efaa106878";

    @Autowired
    FlatMapRepository flatMapRepository;
    private RawDBDemoGeoIPLocationService locationService;

    @Override
    public String getListOfCrytoCurrencies() {
        // coinmarketcap rest api provide list of crypto currencies
        // All crypto price is in USD by default
        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        paratmers.add(new BasicNameValuePair("start","1"));
        paratmers.add(new BasicNameValuePair("limit","500"));
        paratmers.add(new BasicNameValuePair("convert","USD"));
        try {
            String result = makeAPICall(uri, paratmers);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error: Invalid URL " + e.toString());
        }
    }

    @Override
    public CryptoApi getSelectedCrypto(String symbol, String ipAddress) throws IOException, GeoIp2Exception {
        String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest" + "?symbol=" + symbol;
        CryptoApi cryptoApi = null;
        String currCode = null;
        if(ipAddress.length() > 0 ) {
            // if IP address
            //return Geo Location from Geolite service
            RawDBDemoGeoIPLocationService locationService = new RawDBDemoGeoIPLocationService();
            GeoIP geoIP = locationService.getLocation(ipAddress);
            //return crypto currency code like EUR or USD from ISO code
            currCode = Currency.getInstance(new Locale("", geoIP.getISOCode())).getCurrencyCode();
        } else {
            String ip = getClientIPAddress();
            currCode = "EUR";
        }

            try {
                List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
                //coinmarketcap api will return the current price of crypto currency based on currency code currCode
                paratmers.add(new BasicNameValuePair("convert", currCode));
                String result = makeAPICall(uri, paratmers);

                JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                JsonElement je = jsonObject.get("data").getAsJsonObject().get(symbol).getAsJsonObject();
                cryptoApi = new CryptoApi();
                cryptoApi.setSymbol(je.getAsJsonObject().get("symbol").getAsString());
                cryptoApi.setCryptoCurrencyName(je.getAsJsonObject().get("name").getAsString());
                cryptoApi.setPrice(je.getAsJsonObject().get("quote").getAsJsonObject().get(currCode).getAsJsonObject().get("price").getAsDouble());
                FlatMap flatMap = flatMapRepository.findFirstByCurrencySymbol(currCode);
                cryptoApi.setSign(flatMap.getCurrencySign());
                return cryptoApi;
            } catch (IOException e) {
                throw new RuntimeException("Error: cannont access content - " + e.toString());
            } catch (URISyntaxException e) {
                throw new RuntimeException("Error: Invalid URL " + e.toString());
            }

    }

    // Get IP Address from HTTP request
    public static String getClientIPAddress(){
        if(RequestContextHolder.getRequestAttributes() == null){
            return "0.0.0.0";
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        for(String header: IP_HEADER_CANDIDATES){
            String ipList = request.getHeader(header);
            if(ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)){
                String ip = ipList.split(",")[0];
                return ip;
            }
        }

        return request.getRemoteAddr();

    }


    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client_IP",
            "WL-Proxy-Client_IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP+VIA",
            "REMOTE_ADDR"
    };

    //Saving crypto currencies sign symbol in-memory
    @Override
    public List<FlatMap> saveFlatMap() {
        String uri = "https://pro-api.coinmarketcap.com/v1/fiat/map";
        List<FlatMap> flatMaps = new ArrayList<>();
        FlatMap flatMap = null;
        try {
            List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
            paratmers.add(new BasicNameValuePair("start","1"));
            paratmers.add(new BasicNameValuePair("limit","5000"));
            String result = makeAPICall(uri, paratmers);
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            for (JsonElement j : jsonObject.get("data").getAsJsonArray()){
                flatMap = new FlatMap();
                flatMap.setCurrencyName(j.getAsJsonObject().get("name").getAsString());
                flatMap.setCurrencySymbol(j.getAsJsonObject().get("symbol").getAsString());
                flatMap.setCurrencySign(j.getAsJsonObject().get("sign").getAsString());
                flatMaps.add(flatMap);
                flatMapRepository.save(flatMap);
            }
            return flatMaps;
        } catch (IOException e) {
            throw new RuntimeException("Error: cannont access content - " + e.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error: Invalid URL " + e.toString());
        }
    }


    public static String makeAPICall(String uri, List<NameValuePair> parameters)
            throws URISyntaxException, IOException {
        String response_content = "";
        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        // passing token for accessing coin make cap apis
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);
        CloseableHttpResponse response = client.execute(request);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return response_content;
    }


}
