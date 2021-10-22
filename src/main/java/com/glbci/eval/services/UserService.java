package com.glbci.eval.services;

import com.glbci.eval.exceptions.AlreadyExistsException;
import com.glbci.eval.exceptions.BadRequestException;
import com.glbci.eval.exceptions.NotFoundException;
import com.glbci.eval.model.MessageResponse;
import com.glbci.eval.model.Phone;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.PhoneDTO;
import com.glbci.eval.model.dto.UserDTO;
import com.glbci.eval.repositories.PhoneRepository;
import com.glbci.eval.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public UserDTO saveUser(UserDTO userToSave){
        User user = new User();
        user.setId(createId());
        user.setName(userToSave.getName());

        String email = userToSave.getEmail();
        if(validateEmail(email)){
            if(!userRepository.existsByEmail(email)){
                user.setEmail(userToSave.getEmail());
            }else{
                String message = email + " is already in use";
                LOGGER.info(message);
                throw new AlreadyExistsException(message);
            }
        }else{
            String message = email + " is not a valid Email format";
            LOGGER.info(message);
            throw new BadRequestException(message);
        }

        String pwd = userToSave.getPassword();
        if(validatePwd(pwd)){
            user.setPassword(encode(pwd));
        }else{
            String message = "Password doesn't follow the correct format (One upercase letter, lowercase case letters and two digits.";
            LOGGER.info(message);
            throw new BadRequestException(message);
        }
        Date date = new Date();
        user.setCreated(date);
        user.setModified(date);
        user.setLastLogin(date);

        user.setToken("temp");
        user.setActive(true);

        List<Phone> phones = new ArrayList<>();
        for(PhoneDTO phone : userToSave.getPhones()){
            Phone phoneToSave = new Phone(phone.getNumber(), phone.getCitycode(), phone.getCountrycode(), user);
            phones.add(phoneToSave);
        }
        user.setPhones(phones);

        user = userRepository.save(user);
        return convertUserToDto(user);
    }

    public UserDTO getUserById(String uid) {
        User user = userRepository.findById(uid);
        if(user != null){
            return convertUserToDto(user);
        }else{
            throw new NotFoundException("User with ID " + uid + " doesn't exists.");
        }
    }

    public MessageResponse deleteUserById(String uid) {
        User user = new User();
        user = userRepository.findById(uid);
        if(user != null){
            userRepository.delete(user);
        }else{
            String message = "User with ID " + uid + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
        return new MessageResponse("User with ID " + uid + " was deleted.", new Date());
    }

    public UserDTO updateUser(UserDTO userToUpdate){
        User user = userRepository.findById(userToUpdate.getId());

        if(user != null){
            user.setName(userToUpdate.getName());

            String email = userToUpdate.getEmail();
            if(validateEmail(email)){
                user.setEmail(userToUpdate.getEmail());
            }else{
                String message = email + " is not a valid Email format";
                LOGGER.info(message);
                throw new BadRequestException(message);
            }

            String pwd = userToUpdate.getPassword();
            if(validatePwd(pwd)){
                user.setPassword(encode(pwd));
            }else{
                String message = "Password doesn't follow the correct format (One upercase letter, lowercase case letters and two digits.";
                LOGGER.info(message);
                throw new BadRequestException(message);
            }

            Date date = new Date();
            user.setModified(date);
            user.setToken(userToUpdate.getToken());
            user.setActive(userToUpdate.getActive());

            List<Phone> phones = user.getPhones();
            phones.forEach(phone -> phoneRepository.delete(phone));
            user.setPhones(phones);
            user = userRepository.save(user);
            return convertUserToDto(user);
        }else{
            String message = "User with ID " + userToUpdate.getId() + " doesn't exists.";
            LOGGER.info(message);
            throw new NotFoundException(message);
        }
    }

    private String createId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private Boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Boolean validatePwd(String pwd){
        Pattern pattern = Pattern.compile(PWD_REGEX);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }

    private UserDTO convertUserToDto(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPassword(decode(userDTO.getPassword()));
        return userDTO;
    }

    private PhoneDTO convertPhoneToDto(Phone phone) {
        PhoneDTO phoneDTO = modelMapper.map(phone, PhoneDTO.class);
        return phoneDTO;
    }

    private String encode(String pwd){
        String encodedString = Base64.getEncoder().encodeToString(pwd.getBytes());
        return encodedString;
    }

    private String decode(String pwd64){
        byte[] decodedBytes = Base64.getDecoder().decode(pwd64);
        return new String(decodedBytes);
    }

}
