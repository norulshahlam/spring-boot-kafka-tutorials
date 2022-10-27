package com.shah.libraryserviceproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shah.libraryserviceproducer.domain.LibraryEvent;
import com.shah.libraryserviceproducer.domain.LibraryEventType;
import com.shah.libraryserviceproducer.producer.LibraryEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
@RequestMapping("v1/")
public class LibraryEventsController {

    public static final String NEW_LIBRARY_EVENT_WITH_DEFINED_TOPIC = "new-library-event-with-defined-topic";
    public static final String NEW_LIBRARY_EVENT_WITH_DEFAULT_TOPIC = "new-library-event-with-default-topic";
    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostMapping(NEW_LIBRARY_EVENT_WITH_DEFAULT_TOPIC)
    public ResponseEntity<LibraryEvent> sendNewEventWithDefaultTopic(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        log.info("in sendEventWithDefaultTopic controller");
        /**
         * As this NEW even type is displayed, consumer can know that this event is a newly created resource and can proceed to do further actions
         */
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        libraryEventProducer.sendNewEventWithDefaultTopic(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }


    @PostMapping(NEW_LIBRARY_EVENT_WITH_DEFINED_TOPIC)
    public ResponseEntity<LibraryEvent> sendNewEventWithDefinedTopic(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        log.info("in sendEventWithDefinedTopic controller");
        /**
         * As this NEW even type is displayed, consumer can know that this event is a newly created resource and can proceed to do further actions
         */
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
        libraryEventProducer.sendNewEventWithDefinedTopic(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping(NEW_LIBRARY_EVENT_WITH_DEFINED_TOPIC)
    public ResponseEntity<?> sendNewEventWithDefinedTopicWithKey(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {

        log.info("in sendNewEventWithDefinedTopicWithKey controller");
        /**
         * As this UPDATE even type is displayed, consumer can know that this event is a existing resource and can proceed to do further actions. And since we have a key, the message will be sent through the same partition. This is really important for the ordering of the events because Kafka guarantees ordering only at the partition level.
         */

        if (libraryEvent.getLibraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
        libraryEventProducer.sendNewEventWithDefinedTopic(libraryEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    //PUT
//    @PutMapping("/v1/libraryevent")
//    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
//
//
//        if(libraryEvent.getLibraryEventId()==null){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
//        }
//
//        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
//        libraryEventProducer.sendLibraryEvent_Approach2(libraryEvent);
//        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
//    }
}
