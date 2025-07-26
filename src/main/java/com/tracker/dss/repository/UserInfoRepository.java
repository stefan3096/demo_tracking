package com.tracker.dss.repository;

import com.tracker.dss.model.UserInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserInfoRepository extends R2dbcRepository<UserInfo, String> {
    Mono<UserInfo> findByUsername(String username);
    Mono<UserInfo>findByEmail(String email);
    Mono<UserInfo> findByNik(Long nik);
    Mono<UserInfo> findByWorkerId(String workerId);
}
