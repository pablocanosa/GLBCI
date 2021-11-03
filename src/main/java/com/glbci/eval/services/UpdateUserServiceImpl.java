package com.glbci.eval.services;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.repositories.UserRepository;
import com.glbci.eval.utils.UserBuilderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UpdateUserServiceImpl implements UpdateUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBuilderUtils userBuilderUtils;

    public MessageResponseDTO updateUser(String uid, UserRequestDTO userToUpdate) {
        User user = userRepository.findById(uid);
        if (user != null) {
            user = userBuilderUtils.buildUser(userToUpdate, user, uid);
            userRepository.save(user);
            return new MessageResponseDTO("User with ID " + uid + " was updated.", LocalDateTime.now());
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }
}
