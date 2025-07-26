package com.tracker.dss.service.impl;

import com.tracker.dss.model.Transaction;
import com.tracker.dss.model.redis.TransactionData;
import com.tracker.dss.repository.redis.TransactionRedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionRedisServiceimpl {
    @Autowired
    private TransactionRedisRepository trxredisRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public void SaveTransaction(TransactionData transaction){
        trxredisRepository.save(transaction);

        if (transaction.getCustomerId() != null) {
            String key = "index:transaction:customerId:" + transaction.getCustomerId();
            redisTemplate.opsForSet().add(key, transaction.getId());
        }
    }

    public List<TransactionData> findAllByCustomerId(String customerId) {
        String key = "index:transaction:customerId:" + customerId;

        Set<String> transactionIds = redisTemplate.opsForSet().members(key);

        if (transactionIds == null || transactionIds.isEmpty()) {
            return new ArrayList<>();
        }

        Iterable<TransactionData> transactions = trxredisRepository.findAllById(transactionIds);

        return StreamSupport.stream(transactions.spliterator(), false)
                .collect(Collectors.toList());
    }

}
