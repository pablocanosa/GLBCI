package com.glbci.eval.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.dto.PhoneDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.services.UserService;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UpdateUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private static final String UID = "id1234";

    @Test
    void updateUserOK_statusCode200() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();
        MessageResponseDTO messageResponseDTO = new MessageResponseDTO("User with ID " + UID + " was updated.", LocalDateTime.now());

        when(userService.updateUser(anyString(), any())).thenReturn(messageResponseDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/users/" + UID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with ID " + UID + " was updated.")))
                .andExpect(jsonPath("$.date", notNullValue()))
                .andReturn();

        verify(userService, times(1)).updateUser(anyString(), any());
    }

    @Test
    void updateUserWrongEmailFormat_statusCode400() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        when(userService.updateUser(anyString(), any())).thenThrow(new BadRequestException("is not a valid Email format"));
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/users/" + UID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("is not a valid Email format")))
                .andReturn();

        verify(userService, times(1)).updateUser(anyString(), any());
    }

    @Test
    void updateUserWrongPasswordFormat_statusCode400() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        when(userService.updateUser(anyString(), any())).thenThrow(new BadRequestException("Password doesn't follow the correct format (One upercase letter, lowercase case letters and two digits."));
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/users/" + UID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("Password doesn't follow the correct format (One upercase letter, lowercase case letters and two digits.")))
                .andReturn();

        verify(userService, times(1)).updateUser(anyString(), any());
    }

    @Test
    void updateUserNotFound_statusCode404() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        when(userService.updateUser(anyString(), any())).thenThrow(new NotFoundException("doesn't exists."));
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/users/" + UID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("doesn't exists.")))
                .andReturn();

        verify(userService, times(1)).updateUser(anyString(), any());
    }


    private UserRequestDTO createUserRequestDTO() {
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        phoneDTOList.add(new PhoneDTO("22223333", "11", "54"));
        return new UserRequestDTO("Jorge Test", "jorgetest@gmail.com", "Pass99", phoneDTOList);
    }
}
