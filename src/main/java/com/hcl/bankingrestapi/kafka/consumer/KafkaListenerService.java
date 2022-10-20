package com.hcl.bankingrestapi.kafka.consumer;

import com.hcl.bankingrestapi.kafka.dto.LogMessage;
import com.hcl.bankingrestapi.log.entity.LogDetail;
import com.hcl.bankingrestapi.log.mapper.LogMapper;
import com.hcl.bankingrestapi.log.service.entityservice.LogDetailEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListenerService {

    private final LogDetailEntityService logDetailEntityService;

    /**
     * The @KafkaListener annotation allows us to create a listener (I call it a listener),
     * which is where we subscribe to messages coming from the topic.
     * @param logMessage
     */
    @KafkaListener(
            topics = "${kafka.topic}",
            groupId = "${kafka.group-id}"
    )
    public void listen(@Payload LogMessage logMessage) {
        log.info("Message received by consumer..." + logMessage.getMessage());
        saveLogToDatabase(logMessage);
    }

    /**
     * @param logMessage
     */
    @Transactional
    private void saveLogToDatabase(LogMessage logMessage) {
        LogDetail logDetail = LogMapper.INSTANCE.convertToLogDetail(logMessage);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("*****************");
        logDetail = logDetailEntityService.save(logDetail);
        System.out.println("end");
    }
}
