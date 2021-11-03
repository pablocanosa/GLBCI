package com.glbci.eval.services;

import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.repositories.UserRepository;
import com.glbci.eval.utils.UserBuilderUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserServiceImpl implements CreateUserService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBuilderUtils userBuilderUtils;

    public UserResponseDTO saveUser(UserRequestDTO userToSave) {
        User user = userBuilderUtils.buildUser(userToSave, new User(), null);
        userRepository.save(user);
        return convertUserToUserResponseDto(user);
    }

    private UserResponseDTO convertUserToUserResponseDto(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
