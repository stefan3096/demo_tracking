package com.tracker.dss.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Data
@Component
public class MongoConfigData {

  @Value("${spring.data.mongodb.uri}")
  private String mongodbUri;

  @Value("${spring.data.mongodb.connectTimeout:1800}")
  private Integer connectTimeout;

  @Value("${spring.data.mongodb.readTimeout:1000}")
  private Integer readTimeout;

  @Value("${spring.data.mongodb.maxWaitTime:10}")
  private Integer maxWaitTime;

  @Value("${spring.data.mongodb.maxConnectionIdleTime:10}")
  private Integer maxConnectionIdleTime;

  @Value("${spring.data.mongodb.maxConnecting:200}")
  private Integer maxConnecting;

  @Value("${spring.data.mongodb.minSize:0}")
  private Integer minSize;

  @Value("${spring.data.mongodb.maxSize:1000}")
  private Integer maxSize;
}
