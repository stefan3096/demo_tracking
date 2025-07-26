package com.tracker.dss.repository;

import com.tracker.dss.model.StatusDetail;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StatusDetailRepository extends ReactiveMongoRepository<StatusDetail, String> {

    Mono<StatusDetail> findByCode(String statusCode);

}
