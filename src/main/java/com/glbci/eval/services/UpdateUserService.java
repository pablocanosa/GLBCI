package com.glbci.eval.services;

import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.dto.UserRequestDTO;

public interface UpdateUserService {

    MessageResponseDTO updateUser(String uid, UserRequestDTO userToUpdate);
}
