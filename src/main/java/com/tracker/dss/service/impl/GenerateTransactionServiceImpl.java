package com.tracker.dss.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.model.Transaction;
import com.tracker.dss.model.UserInfo;
import com.tracker.dss.repository.UserInfoRepository;
import com.tracker.dss.service.GenerateTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerateTransactionServiceImpl  implements GenerateTransaction {

    private final UserInfoRepository userInfoRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void transaction(TransactionRequest transactionRequest) {
     userInfoRepository.findByNik(transactionRequest.getNik())
             .flatMap(userInfo ->{
                 log.info("Found user by NIK: {}", userInfo.getUsername());
                 Transaction transaction = objectMapper.convertValue(transactionRequest, Transaction.class);
                 transaction.setId(UUID.randomUUID().toString());
                 transaction.setCustomerId(userInfo.getUsername());
                 transaction.setMessage(transactionRequest.getMessage());



                 return Mono.just(userInfo);
             })
             .switchIfEmpty(userInfoRepository.findByEmail(transactionRequest.getSenderEmail()))
             .switchIfEmpty(userInfoRepository.findByUsername(transactionRequest.getSenderName()))
             .switchIfEmpty(Mono.fromRunnable(() ->
                     log.warn("No user found for NIK, Email, or Username.")
             )).subscribe();

    }


}
