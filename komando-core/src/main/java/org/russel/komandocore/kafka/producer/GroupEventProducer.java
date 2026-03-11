package org.russel.komandocore.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String GROUP_TOPIC = "group-events";

    public void publish(Object event) {
        kafkaTemplate.send(GROUP_TOPIC, event);
    }
}
