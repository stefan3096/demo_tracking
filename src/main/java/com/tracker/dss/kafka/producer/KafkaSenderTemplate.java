package com.tracker.dss.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tracker.dss.config.StaticParameter;
import com.tracker.dss.dto.KafkaMessage;
import com.tracker.dss.service.impl.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

@Slf4j
@Service
public class KafkaSenderTemplate {

    @Value("${api.sender.topic}")
    private String senderTopic;

    private final KafkaSender<Object,String> kafkaSender;
    @Autowired
    LogService logService;

    @Autowired
    public KafkaSenderTemplate(KafkaSender<Object, String> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    public Flux<SenderResult<Long>> send (Flux<SenderRecord<Object, String, Long>> recordFlux) {
       return kafkaSender.send(recordFlux);
    }


    public Flux<SenderRecord<Object, String, Object>> createOutBoundFlux(Object messageValue){
        return Flux.just(messageValue)
                .map(i -> {
//                    logService.log(new ESLog(messageValue));
                    return SenderRecord.create(senderTopic,null,System.currentTimeMillis(),null,new Gson().toJson(i),null);
                })
                .mergeWith(Flux.just(messageValue)
                        .map(o -> SenderRecord.create("stream_dss-reqreply_api-bigdata",null,System.currentTimeMillis(),null,new Gson().toJson(o),null)));
    }
    public void sendMessage(KafkaMessage messageValue) {
        kafkaSender.send(createOutBoundFlux(messageValue))
                .doOnNext(result -> log.info("Message sent to Kafka, topic: {}, offset: {}",
                        result.recordMetadata().topic(),
                        result.recordMetadata().offset()))
                .subscribe(); // fire-and-forget
    }
}
