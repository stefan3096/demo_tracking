package com.tracker.dss.repository;

import com.tracker.dss.model.UserInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserInfoRepository extends R2dbcRepository<UserInfo, Long> {
    Mono<UserInfo> findByUsername(String username);
    Mono<UserInfo>findByEmail(String email);
    Mono<UserInfo> findByNik(Long nik);
}
