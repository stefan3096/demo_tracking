package com.tracker.dss.service;

import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.model.Transaction;
import com.tracker.dss.model.redis.TransactionData;
import reactor.core.publisher.Mono;

public interface GenerateTransaction {

    Mono<Transaction> transaction(TransactionRequest transactionRequest);

}
