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
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.MicrometerConsumerListener;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReactorReceiver {


    private final KafkaReceiver<Long, String> kafkaReceiver;
    private final LogService logService;
    private final GenerateTransaction generateTransaction;

    @Bean
    ApplicationRunner runner() {
        return args -> processor();
    }


    @Bean
    MicrometerConsumerListener<Long, String> consumerListener(MeterRegistry registry) {
        return new MicrometerConsumerListener<>(registry);
    }



    public void processor(){
        kafkaReceiver
                .receive()
                .subscribe(r -> {
                    AtomicLong triggeredTime = new AtomicLong(0L);
                    triggeredTime.set(System.currentTimeMillis());

                    KafkaMessage message;
                    try {
                        message = new ObjectMapper().readValue(r.value(), KafkaMessage.class);
                        logService.log(new ESLog(message));

                        if (message.getClientCode().equals("Core-Dss")) {
                            if (null != message.getMethod()) {
                                log.info("new message : {}", message.getMethod());
                                if (message.getMethod().equals("Generate New Transacton ")) {
                                    TransactionRequest dataTransactionRequest = new ObjectMapper().convertValue(message.getData(), new TypeReference<>() {
                                    });
                                    generateTransaction.transaction(dataTransactionRequest);
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

    }
}
