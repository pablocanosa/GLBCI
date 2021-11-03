package com.glbci.eval.services;

import com.glbci.eval.model.dto.MessageResponseDTO;

public interface DeleteUserService {

    MessageResponseDTO deleteUserById(String uid);
}
