package com.glbci.eval.services;

import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.dto.MessageResponseDTO;
import com.glbci.eval.model.Phone;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.GetUserResponseDTO;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.repositories.PhoneRepository;
import com.glbci.eval.repositories.UserRepository;
import com.glbci.eval.utils.Base64Utils;
import com.glbci.eval.utils.FieldValidationUtils;
import com.glbci.eval.utils.JwtUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private Base64Utils base64Utils;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private FieldValidationUtils fieldValidationUtils;

    public UserResponseDTO saveUser(UserRequestDTO userToSave) {
        User user = new User();
        return convertUserToUserResponseDto(buildUser(userToSave, user, null));
    }

    public GetUserResponseDTO getUserById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            return convertUserToGetResponseDto(user);
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    public MessageResponseDTO deleteUserById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            userRepository.delete(user);
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
        return new MessageResponseDTO("User with ID " + uid + " was deleted.", LocalDateTime.now());
    }

    public MessageResponseDTO updateUser(String uid, UserRequestDTO userToUpdate) {
        User user = userRepository.findById(uid);
        if (user != null) {
            return new MessageResponseDTO("User with ID " + uid + " was updated.", LocalDateTime.now());
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    public MessageResponseDTO enablerById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            user.setIsActive(!user.getIsActive());
            LocalDateTime date = LocalDateTime.now();
            user.setModified(date);
            cleanPhones(user);
            user.setPhones(user.getPhones());
            return new MessageResponseDTO("User with ID " + uid + " was updated.", LocalDateTime.now());
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    private User buildUser(UserRequestDTO userToSave, User user, String uid) {

        if (uid == null) {
            user.setId(UUID.randomUUID().toString());
        }

        user.setName(userToSave.getName());

        String email = userToSave.getEmail();
        if (fieldValidationUtils.validateEmail(email)) {
            if (uid == null && !userRepository.existsByEmail(email)) {
                user.setEmail(userToSave.getEmail());
            } else if (uid != null) {
                user.setEmail(userToSave.getEmail());
            } else {
                String message = email + " is already in use";
                LOGGER.info(message);
                throw new AlreadyExistsException(message);
            }
        } else {
            String message = email + " is not a valid Email format";
            LOGGER.info(message);
            throw new BadRequestException(message);
        }

        String pwd = userToSave.getPassword();
        if (fieldValidationUtils.validatePwd(pwd)) {
            user.setPassword(base64Utils.encode(pwd));
        } else {
            String message = "Password doesn't follow the correct format (One upercase letter, lowercase case letters and two digits.";
            LOGGER.info(message);
            throw new BadRequestException(message);
        }

        LocalDateTime date = LocalDateTime.now();
        if (uid == null) {
            user.setCreated(date);
            user.setModified(date);
            user.setLastLogin(date);
            user.setToken(jwtUtils.generateToken(email));
            user.setIsActive(true);

            List<Phone> phones = new ArrayList<>();
            userToSave.getPhones().forEach(phone -> phones.add(new Phone(phone.getNumber(), phone.getCitycode(), phone.getCountrycode(), user)));
            user.setPhones(phones);
        } else {
            user.setModified(date);
            cleanPhones(user);
        }
        return userRepository.save(user);
    }

    private void cleanPhones(User user) {
        List<Phone> phones = user.getPhones();
        phones.forEach(phone -> phoneRepository.delete(phone));
    }

    private UserResponseDTO convertUserToUserResponseDto(User user) {
        UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
        return userResponseDTO;
    }

    private GetUserResponseDTO convertUserToGetResponseDto(User user) {
        GetUserResponseDTO getUserResponseDTO = modelMapper.map(user, GetUserResponseDTO.class);
        getUserResponseDTO.setPassword(base64Utils.decode(getUserResponseDTO.getPassword()));
        return getUserResponseDTO;
    }

}
