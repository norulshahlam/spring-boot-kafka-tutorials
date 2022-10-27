package com.shah.libraryserviceproducer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(LibraryEventsController.class)
@AutoConfigureMockMvc
class LibraryEventsControllerTest {

    @Test
    void sendEventWithDefaultTopic() {
    }

    @Test
    void sendEventWithDefinedTopic() {
    }
}