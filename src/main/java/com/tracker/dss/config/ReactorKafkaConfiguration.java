package com.tracker.dss.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactorKafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${receiver.group-id}")
    private String groupIdReceiver;

    @Value("${receiver.client-id}")
    private String clientIdReceiver;

    @Value("${dss.receiver.topic}")
    private String receiverTopic;

    @Bean
    public SenderOptions<Object, String> senderOptions(){
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return SenderOptions.<Object,String>create(producerProps).maxInFlight(1024);
    }

    @Bean
    public KafkaSender<Object, String> kafkaSender(){
        return KafkaSender.create(senderOptions());
    }

    @Bean
    public ReceiverOptions<Long, String> receiverOptions(){
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdReceiver);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, clientIdReceiver);
        return ReceiverOptions.<Long, String>create(consumerProps)
                .subscription(Collections.singleton(receiverTopic));
    }

    @Bean
    KafkaReceiver<Long, String> kafkaReceiver(){
        return KafkaReceiver.create(receiverOptions());
    }
}
