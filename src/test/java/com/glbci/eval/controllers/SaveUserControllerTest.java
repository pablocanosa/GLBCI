package com.glbci.eval.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.model.dto.PhoneDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.services.CreateUserService;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SaveUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CreateUserService createUserService;

    @Test
    void saveUserOK_statusCode201() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();
        UserResponseDTO userResponseDTO = new UserResponseDTO("id1234", "token1234567890", true);

        when(createUserService.saveUser(any())).thenReturn(userResponseDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("id1234")))
                .andExpect(jsonPath("$.token", is("token1234567890")))
                .andExpect(jsonPath("$.isActive", is(true)))
                .andReturn();

        verify(createUserService, times(1)).saveUser(any());
    }

    @Test
    void saveUserWrongEmailFormat_statusCode400() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        when(createUserService.saveUser(any())).thenThrow(new BadRequestException("is not a valid Email format"));
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("is not a valid Email format")))
                .andReturn();

        verify(createUserService, times(1)).saveUser(any());
    }

    @Test
    void saveUserEmailAlreadyInUse_statusCode409() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        when(createUserService.saveUser(any())).thenThrow(new AlreadyExistsException("is already in use"));
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("is already in use")))
                .andReturn();

        verify(createUserService, times(1)).saveUser(any());
    }

    @Test
    void saveUserWrongPasswordFormat_statusCode400() throws Exception {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        when(createUserService.saveUser(any())).thenThrow(new BadRequestException("Password doesn't follow the correct format"));
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userRequestDTO));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("Password doesn't follow the correct format")))
                .andReturn();

        verify(createUserService, times(1)).saveUser(any());
    }

    private UserRequestDTO createUserRequestDTO() {
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        phoneDTOList.add(new PhoneDTO("22223333", "11", "54"));
        return new UserRequestDTO("Jorge Test", "jorgetest@gmail.com", "Pass99", phoneDTOList);
    }
}
