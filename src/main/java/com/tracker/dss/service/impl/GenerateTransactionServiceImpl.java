package com.tracker.dss.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.model.DetailBranch;
import com.tracker.dss.model.StatusDetail;
import com.tracker.dss.model.Transaction;
import com.tracker.dss.model.UserInfo;
import com.tracker.dss.model.redis.TransactionData;
import com.tracker.dss.repository.DetailBranchRepository;
import com.tracker.dss.repository.StatusDetailRepository;
import com.tracker.dss.repository.TransactionRepository;
import com.tracker.dss.repository.UserInfoRepository;
import com.tracker.dss.service.GenerateTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenerateTransactionServiceImpl  implements GenerateTransaction {

    private final UserInfoRepository userInfoRepository;
    private final StatusDetailRepository statusDetailRepository;
    private final DetailBranchRepository detailBranchRepository;
    private final ObjectMapper objectMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionRedisServiceimpl transactionRedisService;
    @Override
    public  Mono<Transaction> transaction(TransactionRequest transactionRequest) {
       return findUser(transactionRequest)
                .flatMap(userInfo ->
                        processWorkerAndStatus(userInfo, transactionRequest))
                .switchIfEmpty(
                        Mono.defer(() -> {
                    log.warn("No user found for NIK, Email, or Username.");
                    return Mono.just(new Transaction());
                }));
//                .subscribe(transaction -> log.info("Processed transaction: {}", transaction));
    }

    private Mono<UserInfo> findUser(TransactionRequest req) {
        return userInfoRepository.findByNik(req.getNik())
                .switchIfEmpty(userInfoRepository.findByEmail(req.getSenderEmail()))
                .switchIfEmpty(userInfoRepository.findByUsername(req.getSenderName()));
    }

    private Mono<Transaction> processWorkerAndStatus(UserInfo userInfo, TransactionRequest req) {
        return userInfoRepository.findByWorkerId(req.getWorkerId())
                .flatMap(worker ->
                        statusDetailRepository.findByCode(req.getStatusCode())
                                .flatMap(status -> detailBranchRepository.findDetailBranchByCode(req.getBranchCode()).flatMap(detailBranch -> {
                                    Transaction transaction = buildTransaction(userInfo, worker, req, status,detailBranch);
                                    TransactionData trxDataRedis  = objectMapper.convertValue(transaction, TransactionData.class);
                                            trxDataRedis.setCreatedDate(transaction.getCreatedDate());
                                    transactionRedisService.SaveTransaction(trxDataRedis);
                                    return transactionRepository.save(transaction);
                                        })

                                        .switchIfEmpty(Mono.defer(() -> {
                                            log.warn("No Branch found for Code: {}", req.getStatusCode());
                                            return Mono.empty();
                                        }))
                                )
                                .switchIfEmpty(Mono.defer(() -> {
                                    log.warn("No status found for Status Code: {}", req.getStatusCode());
                                    return Mono.empty();
                                }))
                )
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("No worker found for Worker ID: {}", req.getWorkerId());
                    return Mono.empty();
                }));
    }

    private Transaction buildTransaction(UserInfo userInfo, UserInfo worker, TransactionRequest req, StatusDetail statusDetail, DetailBranch detailBranch) {
        Transaction t = new Transaction();
        t.setId(UUID.randomUUID().toString());
        t.setCustomerId(userInfo.getId());
        t.setUsername(userInfo.getUsername());
        t.setTransactionId("TEST " +  Instant.now().toEpochMilli());
        t.setSenderName(userInfo.getUsername());
        t.setReceiverName(req.getReceiverName());
        t.setMessage(statusDetail.getDescription());
        t.setEvent(statusDetail.getStatus());
        t.setStatusCode(statusDetail.getCode());
        t.setWorkerId(worker.getWorkerId());
        t.setLocation(detailBranch.getAddress());
        t.setLocationCode(detailBranch.getCode());
        t.setWeight(req.getWeight());
        t.setCreatedDate(OffsetDateTime.now());
        return t;
    }

    public String getCustomerNameSlug(String username) {
        if (username == null) return null;
        return username
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }


}
