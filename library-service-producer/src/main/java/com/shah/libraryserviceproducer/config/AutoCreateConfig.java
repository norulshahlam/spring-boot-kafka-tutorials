package com.shah.libraryserviceproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class AutoCreateConfig {

    @Bean
    public NewTopic libraryEvents(){
        return TopicBuilder.name("library-events")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas","2",
                        "retention.ms","604800000",
                        "segment.bytes","10485760"))
                .build();
    }

}
