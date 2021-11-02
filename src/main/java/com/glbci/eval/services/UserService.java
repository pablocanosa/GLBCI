package com.glbci.eval.services;

import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO saveUser(UserRequestDTO userToSave);

    GetUserResponseDTO getUserById(String uid);

    MessageResponseDTO deleteUserById(String uid);

    MessageResponseDTO updateUser(String uid, UserRequestDTO userToUpdate);

    MessageResponseDTO enablerById(String uid);
}
