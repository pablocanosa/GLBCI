package com.glbci.eval.controllers;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.services.ActivateUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ActiveUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ActivateUserService activateUserService;

    private static final String UID = "id1234";

    @Test
    void activateUserOK_statusCode200() throws Exception {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO("User with ID " + UID + " was updated.", LocalDateTime.now());
        when(activateUserService.enablerById(anyString())).thenReturn(messageResponseDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/users/" + UID);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with ID " + UID + " was updated.")))
                .andExpect(jsonPath("$.date", notNullValue()))
                .andReturn();

        verify(activateUserService, times(1)).enablerById(anyString());
    }

    @Test
    void activateUserNotFound_statusCode404() throws Exception {
        when(activateUserService.enablerById(anyString())).thenThrow(new NotFoundException("User with ID " + UID + " doesn't exists."));
        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/users/" + UID);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("doesn't exists.")))
                .andReturn();

        verify(activateUserService, times(1)).enablerById(anyString());
    }
}
