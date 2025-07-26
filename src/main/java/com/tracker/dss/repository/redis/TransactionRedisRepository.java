package com.tracker.dss.repository.redis;


import com.tracker.dss.model.redis.TransactionData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRedisRepository extends CrudRepository<TransactionData,String> {

List<TransactionData>findAllByCustomerId(String  customerId);




}
