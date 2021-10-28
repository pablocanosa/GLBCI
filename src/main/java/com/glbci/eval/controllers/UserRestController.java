package com.glbci.eval.controllers;

import com.glbci.eval.model.MessageResponse;
import com.glbci.eval.model.dto.UserDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.services.UserService;
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
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody UserDTO userToSave) {
        LOGGER.info("Create User: {}", userToSave);
        UserResponseDTO response = userService.saveUser(userToSave);
        LOGGER.info("User created: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/users/{uid}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String uid) {
        LOGGER.info("Get User: {}", uid);
        UserResponseDTO response = userService.getUserById(uid);
        LOGGER.info("User: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/users/{uid}")
    public ResponseEntity<MessageResponse> deleteUserById(@PathVariable String uid) {
        LOGGER.info("Delete User: {}", uid);
        MessageResponse response = userService.deleteUserById(uid);
        LOGGER.info(response.getMessage());
        return ResponseEntity.ok(response);
    }


    @PutMapping(value = "/users/{uid}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String uid, @RequestBody UserDTO userToUpdate) {
        LOGGER.info("Update User: {}", userToUpdate);
        UserResponseDTO response = userService.updateUser(uid, userToUpdate);
        LOGGER.info("User updated: {}", response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/users/{uid}")
    public ResponseEntity<UserResponseDTO> enableById(@PathVariable String uid) {
        LOGGER.info("Change Active/Disable User: {}", uid);
        UserResponseDTO response = userService.enablerById(uid);
        LOGGER.info("Changed Active/Disable User: {}", response);
        return ResponseEntity.ok(response);
    }
}
