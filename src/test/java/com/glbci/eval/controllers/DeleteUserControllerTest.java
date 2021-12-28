package com.glbci.eval.controllers;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.services.DeleteUserService;
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
public class DeleteUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DeleteUserService deleteUserService;

    private static final String UID = "id1234";

    @Test
    void deleteUserOK_statusCode200() throws Exception {
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO("User with ID " + UID + " was deleted.", LocalDateTime.now());
        when(deleteUserService.deleteUserById(anyString())).thenReturn(messageResponseDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/users/" + UID);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with ID " + UID + " was deleted.")))
                .andExpect(jsonPath("$.date", notNullValue()))
                .andReturn();

        verify(deleteUserService, times(1)).deleteUserById(anyString());
    }

    @Test
    void deleteUserNotFound_statusCode404() throws Exception {
        when(deleteUserService.deleteUserById(anyString())).thenThrow(new NotFoundException("User with ID " + UID + " doesn't exists."));
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/users/" + UID);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("doesn't exists.")))
                .andReturn();

        verify(deleteUserService, times(1)).deleteUserById(anyString());
    }
}
