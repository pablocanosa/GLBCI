package com.glbci.eval.controllers;

import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.services.UserServiceImpl;
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
    private UserServiceImpl userServiceImpl;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody UserRequestDTO userToSave) {
        LOGGER.info("Create User: {}", userToSave);
        UserResponseDTO response = userServiceImpl.saveUser(userToSave);
        LOGGER.info("User created: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/users/{uid}")
    public ResponseEntity<GetUserResponseDTO> getUserById(@PathVariable String uid) {
        LOGGER.info("Get User: {}", uid);
        GetUserResponseDTO response = userServiceImpl.getUserById(uid);
        LOGGER.info("User: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponseDTO> deleteUserById(@PathVariable String uid) {
        LOGGER.info("Delete User: {}", uid);
        MessageResponseDTO response = userServiceImpl.deleteUserById(uid);
        LOGGER.info(response.getMessage());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponseDTO> updateUserById(@PathVariable String uid, @RequestBody UserRequestDTO userToUpdate) {
        LOGGER.info("Update User: {}", userToUpdate);
        MessageResponseDTO response = userServiceImpl.updateUser(uid, userToUpdate);
        LOGGER.info("User updated: {}", response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponseDTO> enableById(@PathVariable String uid) {
        LOGGER.info("Change Active/Disable User: {}", uid);
        MessageResponseDTO response = userServiceImpl.enablerById(uid);
        LOGGER.info("Changed Active/Disable User: {}", response);
        return ResponseEntity.ok(response);
    }
}
