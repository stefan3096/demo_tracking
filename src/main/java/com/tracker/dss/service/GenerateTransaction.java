package com.tracker.dss.service;

import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.model.Transaction;
import com.tracker.dss.model.redis.TransactionData;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface GenerateTransaction {

   void transaction(TransactionRequest transactionRequest);

    Mono<ResponseEntity<String>> publishTransaction(TransactionRequest transactionRequest);


}
