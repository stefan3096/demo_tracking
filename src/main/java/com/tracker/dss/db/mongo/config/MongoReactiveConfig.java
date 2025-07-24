package com.tracker.dss.db.mongo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.tracker.dss.config.MongoConfigData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@EnableReactiveMongoRepositories(basePackages = {"com.tracker.dss.mongo"})
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

  private final MongoConfigData mongoConfigData;

  @Bean
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString(mongoConfigData.getMongodbUri());

    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .applyToSocketSettings(
        block -> block
          .connectTimeout(mongoConfigData.getConnectTimeout(), TimeUnit.MILLISECONDS)
          .readTimeout(mongoConfigData.getReadTimeout(), TimeUnit.MILLISECONDS)
      )
      .applyToConnectionPoolSettings(
        block -> block
          .maxWaitTime(mongoConfigData.getMaxWaitTime(), TimeUnit.SECONDS)
          .maxConnectionIdleTime(mongoConfigData.getMaxConnectionIdleTime(), TimeUnit.SECONDS)
          .maxConnecting(mongoConfigData.getMaxConnecting())
          .minSize(mongoConfigData.getMinSize())
          .maxSize(mongoConfigData.getMaxSize())
      )
      .build();

    return MongoClients.create(mongoClientSettings);
  }

  @NonNull
  @Override
  protected String getDatabaseName() {
    return "dbtrack";
  }
}
