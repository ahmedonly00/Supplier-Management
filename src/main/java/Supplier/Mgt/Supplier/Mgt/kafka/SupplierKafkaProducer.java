package Supplier.Mgt.Supplier.Mgt.kafka;

import Supplier.Mgt.Supplier.Mgt.event.SupplierEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplierKafkaProducer {

    @Value("${kafka.topic.supplier}")
    private String supplierTopic;

    // Spring Boot auto-configures KafkaTemplate using spring.kafka.producer.* in
    // application.yaml
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishEvent(SupplierEvent event) {
        log.info("Publishing Kafka event [{}] for supplier id={}", event.getEventType(), event.getSupplierId());

        // Run on a separate thread so the HTTP response is never blocked by Kafka
        // availability
        CompletableFuture.runAsync(() -> kafkaTemplate.send(supplierTopic, String.valueOf(event.getSupplierId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event [{}] for supplier id={}: {}",
                                event.getEventType(), event.getSupplierId(), ex.getMessage());
                    } else {
                        log.info("Event [{}] published to partition {} at offset {}",
                                event.getEventType(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                }));
    }
}
