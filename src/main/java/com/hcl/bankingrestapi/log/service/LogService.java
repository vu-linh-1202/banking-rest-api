package com.hcl.bankingrestapi.log.service;

import com.hcl.bankingrestapi.kafka.dto.LogMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LogService {

    @Value("${kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, LogMessage> kafkaTemplate;

    public void log(LogMessage logMessage) {
        String id = UUID.randomUUID().toString();
        kafkaTemplate.send(topic, id, logMessage);
    }
}
