package ru.samusev.producer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.samusev.producer.domain.ClientDto;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    @Value("${kafka.client.topic.name}")
    private String userTopic;

    @Value("${kafka.topic.without.partitions.name}")
    private String withoutPartitionTopic;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public String sendClient(ClientDto clientDto) {
        try {
            var userJson = objectMapper.writeValueAsString(clientDto);
            var future = kafkaTemplate.send(userTopic, userJson);
            var result = future.get();
            log.info("Successful send to topic {} partition {}", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());

            return result.getProducerRecord().value();
        } catch (InterruptedException | ExecutionException | JsonProcessingException ex) {
            log.error("Cannot send message to Kafka Topic {}", userTopic, ex);
            throw new RuntimeException("Cannot send message to Kafka Topic {} " + userTopic, ex);
        }
    }

    public String sendToWithoutPartitionTopic(String message) {
        try {
            var future = kafkaTemplate.send(withoutPartitionTopic, message);
            var result = future.get();
            log.info("Successful send to topic {} partition {}", result.getRecordMetadata().topic(), result.getRecordMetadata().partition());

            return result.getProducerRecord().value();
        } catch (InterruptedException | ExecutionException ex) {
            log.error("Cannot send message to Kafka Topic {}", withoutPartitionTopic, ex);
            throw new RuntimeException("Cannot send message to Kafka Topic {} " + withoutPartitionTopic, ex);
        }
    }
}
