package com.glbci.eval.controllers;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.model.dto.PhoneDTO;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GetUserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private static final String UID = "id1234";

    @Test
    void getUserOK_statusCode200() throws Exception {
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        phoneDTOList.add(new PhoneDTO("22223333", "11", "54"));
        LocalDateTime date = LocalDateTime.now();
        GetUserResponseDTO getUserResponseDTO = GetUserResponseDTO.builder()
                .id(UID)
                .token("token123456789")
                .isActive(true)
                .name("Jorge Test")
                .email("jorge@gmail.com")
                .password("Pwd22")
                .phones(phoneDTOList)
                .created(date)
                .lastLogin(date)
                .modified(date)
                .build();

        when(userService.getUserById(anyString())).thenReturn(getUserResponseDTO);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/" + UID);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(UID)))
                .andExpect(jsonPath("$.token", is("token123456789")))
                .andExpect(jsonPath("$.isActive", is(true)))
                .andExpect(jsonPath("$.name", is("Jorge Test")))
                .andExpect(jsonPath("$.email", is("jorge@gmail.com")))
                .andExpect(jsonPath("$.password", is("Pwd22")))
                .andExpect(jsonPath("$.phones", notNullValue()))
                .andExpect(jsonPath("$.created", notNullValue()))
                .andExpect(jsonPath("$.lastLogin", notNullValue()))
                .andExpect(jsonPath("$.modified", notNullValue()))
                .andReturn();

        verify(userService, times(1)).getUserById(anyString());
    }

    @Test
    void getUserNotFound_statusCode404() throws Exception {
        when(userService.getUserById(anyString())).thenThrow(new NotFoundException("User with ID " + UID + " doesn't exists."));
        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users/" + UID);

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("doesn't exists.")))
                .andReturn();

        verify(userService, times(1)).getUserById(anyString());
    }
}
