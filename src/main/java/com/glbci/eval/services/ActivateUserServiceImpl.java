package com.glbci.eval.services;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivateUserServiceImpl implements ActivateUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivateUserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    public MessageResponseDTO enablerById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            user.setIsActive(!user.getIsActive());
            user.setModified(LocalDateTime.now());
            userRepository.save(user);
            return new MessageResponseDTO("User with ID " + uid + " was updated.", LocalDateTime.now());
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }
}
