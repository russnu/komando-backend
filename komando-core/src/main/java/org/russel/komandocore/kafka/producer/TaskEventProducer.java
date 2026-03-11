package org.russel.komandocore.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TASK_TOPIC = "task-events";

    public void publish(Object event) {
        kafkaTemplate.send(TASK_TOPIC, event);
    }
}
