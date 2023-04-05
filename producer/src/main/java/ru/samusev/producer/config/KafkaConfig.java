package ru.samusev.producer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Value("${kafka.client.topic.name}")
    private String userTopic;

    @Value("${kafka.topic.without.partitions.name}")
    private String topicWithoutPartitions;

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(userTopic)
                           .partitions(5)
                           .replicas(1)
                           .build();
    }

    @Bean
    public NewTopic topicWithoutPartitions() {
        return TopicBuilder.name(topicWithoutPartitions)
                           .replicas(1)
                           .build();
    }
}
