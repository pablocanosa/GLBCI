package com.glbci.eval.controllers;

import com.glbci.eval.model.MessageResponse;
import com.glbci.eval.model.dto.UserDTO;
import com.glbci.eval.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userToSave) {
        LOGGER.info("Create User: {}", userToSave.toString());
        UserDTO response = userService.saveUser(userToSave);
        LOGGER.info("User created: {}", response.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/{uid}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String uid) {
        LOGGER.info("Get User: {}", uid);
        UserDTO response = userService.getUserById(uid);
        LOGGER.info("User: {}", response.toString());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{uid}")
    public ResponseEntity<MessageResponse> deleteUserById(@PathVariable String uid) {
        LOGGER.info("Delete User: {}", uid);
        MessageResponse response = userService.deleteUserById(uid);
        LOGGER.info(response.getMessage());
        return ResponseEntity.ok(response);
    }


    @PutMapping(value = "")
    public ResponseEntity<UserDTO> getUserById(@RequestBody UserDTO userToUpdate) throws Exception {
        LOGGER.info("Update User: {}", userToUpdate);
        UserDTO response = userService.updateUser(userToUpdate);
        LOGGER.info("User updated: {}", response.toString());
        return ResponseEntity.ok(response);
    }
}
