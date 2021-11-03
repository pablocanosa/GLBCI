package com.glbci.eval.utils;

import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.model.Phone;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.UserRequestDTO;
import com.glbci.eval.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserBuilderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBuilderUtils.class);

    @Autowired
    private Base64Utils base64Utils;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private FieldValidationUtils fieldValidationUtils;
    @Autowired
    private UserRepository userRepository;

    public User buildUser(UserRequestDTO userToSave, User user, String uid) {

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
                LOGGER.error(message);
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
            LOGGER.error(message);
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
        }
        return user;
    }
}
