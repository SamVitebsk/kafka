package ru.samusev.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.samusev.consumer.domain.UserDto;
import ru.samusev.consumer.entity.Client;
import ru.samusev.consumer.repository.ClientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {
    private static final String USER_TOPIC = "${kafka.topic.name}";
    private static final String TOPIC_WITHOUT_PARTITIONS = "${kafka.topic.without.partitions.name}";

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final ClientRepository clientRepository;

    @KafkaListener(topics = USER_TOPIC)
    public void consumeClientMessage(String message) {
        try {
            var userDto = objectMapper.readValue(message, UserDto.class);
            clientRepository.save(modelMapper.map(userDto, Client.class));
            log.info("Receive new message! {}", message);
        } catch (JsonProcessingException ex) {
            log.error("Consumer exception: ", ex);
            throw new RuntimeException(ex);
        }
    }

    @KafkaListener(topics = TOPIC_WITHOUT_PARTITIONS)
    public void consumeMessage(String message) {
        log.info("Receive new message! {}", message);
    }
}
