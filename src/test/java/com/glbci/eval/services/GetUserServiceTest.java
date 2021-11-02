package com.glbci.eval.services;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.model.dto.PhoneDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GetUserServiceTest {
    @Autowired
    private UserService userService;

    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void init(){
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        phoneDTOList.add(new PhoneDTO("22223333", "11", "54"));
        userResponseDTO = userService.saveUser(new UserRequestDTO("Jorge Test", "jorgetest@gmail.com", "Pass99", phoneDTOList));
    }

    @Test
    void getUserOK(){
        GetUserResponseDTO getUserResponseDTO = userService.getUserById(userResponseDTO.getId());
        Assertions.assertNotNull(getUserResponseDTO);
        Assertions.assertEquals(userResponseDTO.getToken(), getUserResponseDTO.getToken());
    }

    @Test
    void getUserNotFound(){
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            userService.getUserById("wrong-id");
        });
        String expectedMessage = "doesn't exists.";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
