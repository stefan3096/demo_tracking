package com.tracker.dss.controller;

import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.model.Transaction;
import com.tracker.dss.model.redis.TransactionData;
import com.tracker.dss.service.GenerateTransaction;
import com.tracker.dss.service.impl.TransactionRedisServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestingController {

    @Autowired
    private GenerateTransaction generateTransaction;

    @Autowired
    private TransactionRedisServiceimpl transactionRedisServiceimpl;


    @PostMapping("/transaction")
    public Mono<Transaction> postTransaction(@RequestBody TransactionRequest transactionRequest) {
        return generateTransaction.transaction(transactionRequest);
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionData>> getTransactionsByCustomerId(
            @PathVariable String customerId) {
        List<TransactionData> transactions = transactionRedisServiceimpl.findAllByCustomerId(customerId);
        return ResponseEntity.ok(transactions);
    }


}
