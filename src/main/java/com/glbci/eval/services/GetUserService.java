package com.glbci.eval.services;

import com.glbci.eval.model.dto.GetUserResponseDTO;

public interface GetUserService {
    GetUserResponseDTO getUserById(String uid);
}
