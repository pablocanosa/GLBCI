package com.glbci.eval.services;

import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
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
public class SaveUserServiceTest {

    @Autowired
    private CreateUserService createUserService;

    private UserRequestDTO userRequestDTO;

    @BeforeEach
    public void init() {
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        phoneDTOList.add(new PhoneDTO("22223333", "11", "54"));
        userRequestDTO = new UserRequestDTO("Jorge Test", "jorgetest@gmail.com", "Pass99", phoneDTOList);
    }

    @Test
    void saveUserOK() {
        UserResponseDTO userResponseDTO = createUserService.saveUser(userRequestDTO);
        Assertions.assertNotNull(userResponseDTO);
    }

    @Test
    void saveUserWrongEmailFormat() {
        userRequestDTO.setEmail("jorgetestgmail.com");
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            createUserService.saveUser(userRequestDTO);
        });
        String expectedMessage = "is not a valid Email format";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void saveUserEmailAlreadyInUse() {
        UserResponseDTO userResponseDTO = createUserService.saveUser(userRequestDTO);
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> {
            createUserService.saveUser(userRequestDTO);
        });
        String expectedMessage = "is already in use";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void saveUserWrongPasswordFormat() {
        userRequestDTO.setPassword("pwd78");
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            createUserService.saveUser(userRequestDTO);
        });
        String expectedMessage = "Password doesn't follow the correct format (One upercase letter, lowercase case letters and two digits.";
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
