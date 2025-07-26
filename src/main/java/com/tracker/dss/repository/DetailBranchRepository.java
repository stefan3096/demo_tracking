package com.tracker.dss.repository;

import com.tracker.dss.model.DetailBranch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface DetailBranchRepository extends ReactiveMongoRepository<DetailBranch,Integer> {

    Mono<DetailBranch> findDetailBranchByCode(String code);


}
