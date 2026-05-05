package Supplier.Mgt.Supplier.Mgt.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.supplier}")
    private String supplierTopic;

    @Bean
    public NewTopic supplierEventsTopic() {
        return TopicBuilder.name(supplierTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
