package com.glbci.eval.services;

import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.MessageResponse;
import com.glbci.eval.model.Phone;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.UserDTO;
import com.glbci.eval.model.dto.UserResponseDTO;
import com.glbci.eval.repositories.PhoneRepository;
import com.glbci.eval.repositories.UserRepository;
import com.glbci.eval.utils.Base64Utils;
import com.glbci.eval.utils.JwtUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.glbci.eval.constants.Constants.EMAIL_REGEX;
import static com.glbci.eval.constants.Constants.PWD_REGEX;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

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

    public UserResponseDTO saveUser(UserDTO userToSave) {
        User user = new User();
        return convertUserToResponseDto(buildUser(userToSave, user, null));
    }

    public UserResponseDTO getUserById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            return convertUserToResponseDto(user);
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    public MessageResponse deleteUserById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            userRepository.delete(user);
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
        return new MessageResponse("User with ID " + uid + " was deleted.", LocalDateTime.now());
    }

    public UserResponseDTO updateUser(String uid, UserDTO userToUpdate) {
        User user = userRepository.findById(uid);

        if (user != null) {
            return convertUserToResponseDto(buildUser(userToUpdate, user, uid));
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    public UserResponseDTO enablerById(String uid) {
        User user = userRepository.findById(uid);
        if (user != null) {
            user.setIsActive(!user.getIsActive());
            LocalDateTime date = LocalDateTime.now();
            user.setModified(date);
            cleanPhones(user);
            user.setPhones(user.getPhones());
            return convertUserToResponseDto(userRepository.save(user));
        } else {
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    private User buildUser(UserDTO userToSave, User user, String uid) {

        if (uid == null) {
            user.setId(createId());
        }

        user.setName(userToSave.getName());

        String email = userToSave.getEmail();
        if (validateEmail(email)) {
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
        if (validatePwd(pwd)) {
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

    private String createId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validatePwd(String pwd) {
        Pattern pattern = Pattern.compile(PWD_REGEX);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    private UserResponseDTO convertUserToResponseDto(User user) {
        UserResponseDTO userResponseDTO = modelMapper.map(user, UserResponseDTO.class);
        userResponseDTO.setPassword(base64Utils.decode(userResponseDTO.getPassword()));
        return userResponseDTO;
    }
}
