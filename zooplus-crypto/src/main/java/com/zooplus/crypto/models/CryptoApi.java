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
public class CryptoApi {

    private String cryptoCurrencyName;
    private String symbol;
    private double price;
    private String sign;


}
