package com.glbci.eval.services;

import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;

public interface CreateUserService {

    UserResponseDTO saveUser(UserRequestDTO userToSave);
}
