package com.glbci.eval.controllers;

import com.glbci.eval.exceptions.CustomException;
import com.glbci.eval.model.dto.UserDTO;
import com.glbci.eval.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.glbci.eval.model.Error;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserDTO> saveUser(@RequestBody UserDTO userToSave) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(userToSave));
        } catch (Exception e) {
            throw new CustomException(new Error("No esta"));
        }
    }

    @GetMapping(value = "/{uid}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String uid) throws Exception {
        try {
            return ResponseEntity.ok(userService.getUserById(uid));
        } catch (Exception e) {
            throw new CustomException(new Error("No esta"));
        }
    }

//    @PutMapping(value = "")
//    public ResponseEntity<UserDTO> getUserById(@RequestBody UserDTO userToUpdate) throws Exception {
//        try {
//            return ResponseEntity.ok(userService.updateUser(userToUpdate));
//        } catch (Exception e) {
//            System.out.println(e);
//            throw new Exception(e);
//        }
//    }
}
