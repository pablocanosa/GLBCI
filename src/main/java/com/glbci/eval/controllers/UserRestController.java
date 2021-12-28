package com.glbci.eval.controllers;

import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private ActivateUserService activateUserService;

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private DeleteUserService deleteUserService;

    @Autowired
    private GetUserService getUserService;

    @Autowired
    private UpdateUserService updateUserService;

    /**
     * Create a new User
     * @param userToSave, user information in JSON format
     * @return UserResponseDTO, Created ID, token and Active status in JSON format
     * @throws AlreadyExistsException if the User already exists
     * @throws BadRequestException if the email or password have wrong format
     */
    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody UserRequestDTO userToSave) {
        LOGGER.info("Create User: {}", userToSave);
        UserResponseDTO response = createUserService.saveUser(userToSave);
        LOGGER.info("User created: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get a user by his ID
     * @param uid, user ID
     * @return GetUserResponseDTO, all the user information in JSON format
     * @throws NotFoundException if the uid is not found
     */
    @GetMapping(value = "/users/{uid}")
    public ResponseEntity<GetUserResponseDTO> getUserById(@PathVariable String uid) {
        LOGGER.info("Get User: {}", uid);
        GetUserResponseDTO response = getUserService.getUserById(uid);
        LOGGER.info("User: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a user by his ID
     * @param uid, user ID
     * @return MessageResponseDTO, confirmation message
     * @throws NotFoundException if the uid is not found
     */
    @DeleteMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponseDTO> deleteUserById(@PathVariable String uid) {
        LOGGER.info("Delete User: {}", uid);
        MessageResponseDTO response = deleteUserService.deleteUserById(uid);
        LOGGER.info(response.getMessage());
        return ResponseEntity.ok(response);
    }

    /**
     * Update a user by his ID
     * @param uid, user ID
     * @param userToUpdate, user information in JSON format
     * @return MessageResponseDTO, confirmation message
     * @throws NotFoundException if the uid is not found
     * @throws BadRequestException if the email or password have wrong format
     */
    @PutMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponseDTO> updateUserById(@PathVariable String uid, @RequestBody UserRequestDTO userToUpdate) {
        LOGGER.info("Update User: {}", userToUpdate);
        MessageResponseDTO response = updateUserService.updateUser(uid, userToUpdate);
        LOGGER.info("User updated: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Activate or deactivate a user by his ID
     * @param uid, user ID
     * @return MessageResponseDTO, confirmation message
     * @throws NotFoundException if the uid is not found
     */
    @PatchMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponseDTO> enableById(@PathVariable String uid) {
        LOGGER.info("Change Active/Disable User: {}", uid);
        MessageResponseDTO response = activateUserService.enablerById(uid);
        LOGGER.info("Changed Active/Disable User: {}", response);
        return ResponseEntity.ok(response);
    }
}
