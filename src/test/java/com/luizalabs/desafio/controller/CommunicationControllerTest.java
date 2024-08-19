package com.luizalabs.desafio.controller;

import static com.luizalabs.desafio.utils.DTOUtils.getDefaultRequest;
import static com.luizalabs.desafio.utils.DTOUtils.getDefaultResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.luizalabs.desafio.dto.request.CommunicationRequest;
import com.luizalabs.desafio.dto.response.CommunicationResponse;
import com.luizalabs.desafio.service.CommunicationService;

@ExtendWith(MockitoExtension.class)
class CommunicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommunicationService service;

    @InjectMocks
    private CommunicationController controller;

    private ObjectMapper objectMapper;
    private final CommunicationRequest request = getDefaultRequest();
    private final CommunicationResponse response = getDefaultResponse();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createCommunication_success() throws Exception {
        when(service.create(any(CommunicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/communication/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.message").value(response.getMessage()))
            .andExpect(jsonPath("$.receiver").value(response.getReceiver()))
            .andExpect(jsonPath("$.status").value(response.getStatus().toString()));
    }

    @Test
    void getScheduleStatus_success() throws Exception {
        when(service.getScheduleStatusById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/communication/scheduleStatus/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.message").value(response.getMessage()))
            .andExpect(jsonPath("$.receiver").value(response.getReceiver()))
            .andExpect(jsonPath("$.status").value(response.getStatus().toString()));
    }

    @Test
    void cancelSend_success() throws Exception {
        when(service.cancelSendById(anyLong())).thenReturn(response);

        mockMvc.perform(get("/communication/cancelSend/{id}", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(response.getId()))
            .andExpect(jsonPath("$.message").value(response.getMessage()))
            .andExpect(jsonPath("$.receiver").value(response.getReceiver()))
            .andExpect(jsonPath("$.status").value(response.getStatus().toString()));
    }

    @Test
    void sendCommunication_success() throws Exception {
        mockMvc.perform(get("/communication/sendToQueue/{id}", 1L))
            .andExpect(status().isOk());
    }
}

