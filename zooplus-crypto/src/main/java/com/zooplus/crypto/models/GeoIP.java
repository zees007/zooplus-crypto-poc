package com.zooplus.crypto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author mhmdz
 * Created By Zeeshan on 26-04-2022
 * @project zooplus-crypto
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GeoIP {

    private String ipAddress;
    private String ISOCode;
    private String countryName;

}
