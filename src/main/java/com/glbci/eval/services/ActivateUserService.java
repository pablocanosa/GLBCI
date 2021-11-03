package com.glbci.eval.services;

import com.glbci.eval.model.dto.MessageResponseDTO;

public interface ActivateUserService {

    MessageResponseDTO enablerById(String uid);
}
