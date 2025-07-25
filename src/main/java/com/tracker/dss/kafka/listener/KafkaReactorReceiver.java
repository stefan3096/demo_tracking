package com.tracker.dss.kafka.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.dss.dto.KafkaMessage;

import com.tracker.dss.dto.TransactionRequest;
import com.tracker.dss.model.ESLog;
import com.tracker.dss.service.GenerateTransaction;
import com.tracker.dss.service.impl.LogService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReactorReceiver {


    private final KafkaReceiver<Long, String> kafkaReceiver;
    private final LogService logService;
    private final GenerateTransaction generateTransaction;

    @Bean
    ApplicationRunner runner(MicrometerConsumerListener<Long,String> consumerListener){
        return args -> {
            processor(consumerListener);
        };
    }

    @Bean
    MicrometerConsumerListener<Long, String> consumerListener(MeterRegistry registry) {
        return new MicrometerConsumerListener<>(registry);
    }



    public void processor(MicrometerConsumerListener<Long,String> consumerListener){
        kafkaReceiver
                .receive()
                .subscribe(r -> {
                    AtomicLong triggeredTime = new AtomicLong(0L);
                    triggeredTime.set(System.currentTimeMillis());

                    KafkaMessage message = null;

                    Flux<Object> messageValue = Flux.empty();
                    try {
                        message = new ObjectMapper().readValue(r.value(), KafkaMessage.class);
                        logService.log(new ESLog(message));

                        if (null != message) {
                            if (message.getClientCode().equals("Core-Dss")) {
                                KafkaMessage finalMessage = message;
                                if (null != finalMessage.getMethod()) {
                                    log.info("new message : {}", finalMessage.getMethod());
                                    switch (finalMessage.getMethod()) {
                                        case "Generate New Transacton ":
                                            TransactionRequest dataTransactionRequest = new ObjectMapper().convertValue(finalMessage.getData(), new TypeReference<>(){});
                                            generateTransaction.transaction(dataTransactionRequest);
                                            break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

    }
}
