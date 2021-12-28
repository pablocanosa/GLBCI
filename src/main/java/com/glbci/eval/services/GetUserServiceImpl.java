package com.glbci.eval.services;

import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.repositories.UserRepository;
import com.glbci.eval.utils.Base64Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserServiceImpl implements GetUserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Base64Utils base64Utils;

    public GetUserResponseDTO getUserById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            return convertUserToGetResponseDto(user);
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.error(message);
            throw new NotFoundException(message);
        }
    }

    private GetUserResponseDTO convertUserToGetResponseDto(User user) {
        GetUserResponseDTO getUserResponseDTO = modelMapper.map(user, GetUserResponseDTO.class);
        return getUserResponseDTO;
    }

}
