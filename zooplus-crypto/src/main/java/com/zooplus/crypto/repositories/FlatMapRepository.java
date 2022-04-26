package com.zooplus.crypto.repositories;

import com.zooplus.crypto.models.FlatMap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author mhmdz
 * Created By Zeeshan on 26-04-2022
 * @project zooplus-crypto
 */


@Repository
public interface FlatMapRepository extends CrudRepository<FlatMap, Integer> {

    FlatMap findByCurrencySymbol(String currCode);
}
