package com.shah.libraryserviceproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.libraryserviceproducer.domain.Book;
import com.shah.libraryserviceproducer.domain.LibraryEvent;
import com.shah.libraryserviceproducer.domain.LibraryEventType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@EnableScheduling
public class LibraryEventProducer {

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    String TOPIC = "library-events";
    @Autowired
    ObjectMapper objectMapper;

    /**
     * @param libraryEvent
     * @throws JsonProcessingException
     * @description This is the default method for sending event. The method sendEevent() doesn't require to insert topic name. as it will follow the one in application property file
     */
    public void sendNewEventWithDefaultTopic(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        kafkaTemplate.sendDefault(key, value)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        handleFailure(ex);
                    } else {
                        handleSuccess(key, value, result);
                    }
                });
    }

    /**
     * @param libraryEvent
     * @throws JsonProcessingException
     * @description This is how you send event with topics defined in arguments using the send() method
     */
    public void sendNewEventWithDefinedTopic(LibraryEvent libraryEvent) throws JsonProcessingException {

        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);

        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, TOPIC);
        kafkaTemplate.send(producerRecord)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        handleFailure(ex);
                    } else {
                        handleSuccess(key, value, result);
                    }
                });
    }

    /**
     * @param key
     * @param value
     * @param topic
     * @return
     * @description We can also add record headers when sending event. Record headers give you the ability to add some metadata about the Kafka record, without adding any extra information to the key/value pair of the record itself. Consider if you wanted to embed some information in a message, such as an identifier for the system from which the data originated
     */
    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }

    private void handleFailure(Throwable ex) {
        log.error("Error Sending the Message and the exception is {}", ex.getMessage());
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message Sent SuccessFully for the key [{}] for value: [{}], and partition [{}]", key, value, result.getRecordMetadata().partition());
    }

    // create sample event
//    @Scheduled(fixedRate = 1000)
    public void sendSampleEvent() throws JsonProcessingException {
        int libraryEventId = (int) (Math.random() * 1000);
        LibraryEvent libraryEvent = LibraryEvent.builder()
                .libraryEventId(libraryEventId)
                .libraryEventType(LibraryEventType.NEW)
                .book(Book.builder()
                        .bookId(libraryEventId)
                        // Generate random book name
                        .bookName("Book-" + libraryEventId)
                        .bookAuthor("Dilip Shah")
                        .build())
                .build();
        ProducerRecord<Integer, String> record = buildProducerRecord(libraryEvent.getLibraryEventId(), objectMapper.writeValueAsString(libraryEvent), TOPIC);
        kafkaTemplate.send(record);
    }
}
