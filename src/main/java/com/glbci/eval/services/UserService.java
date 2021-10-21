package com.glbci.eval.services;

import com.glbci.eval.exceptions.CustomException;
import com.glbci.eval.model.Error;
import com.glbci.eval.model.Phone;
import com.glbci.eval.model.User;
import com.glbci.eval.model.dto.PhoneDTO;
import com.glbci.eval.model.dto.UserDTO;
import com.glbci.eval.repositories.PhoneRepository;
import com.glbci.eval.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.glbci.eval.constants.Constants.EMAIL_REGEX;
import static com.glbci.eval.constants.Constants.PWD_REGEX;

@Service
public class UserService {

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
            user.setEmail(userToSave.getEmail());
        }else{
            System.out.println("error");
        }
        user.setPassword(userToSave.getPassword());

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

    public UserDTO getUserById(String uid) throws Exception {

        User user = userRepository.findById(uid);
        if(user != null){
            return convertUserToDto(user);
        }else{
            throw new CustomException(new Error("No esta"));
        }
    }

//    public UserDTO updateUser(UserDTO userToUpdate){
//        User user = userRepository.findById(userToUpdate.getId());
//
//        if(user != null){
//            user.setName(userToUpdate.getName());
//            String email = userToUpdate.getEmail();
//            user.setPassword(userToUpdate.getPassword());
//            if(validateEmail(email)){
//                user.setEmail(userToUpdate.getEmail());
//            }else{
//                System.out.println("error");
//            }
//
//            Date date = new Date();
//            user.setModified(date);
//            user.setToken(userToUpdate.getToken());
//            user.setActive(userToUpdate.getActive());
//
//            List<Phone> phones = user.getPhones();
//
//            convertPhoneToDto
//
//
//            for(PhoneDTO phone : userToUpdate.getPhones()){
//                Phone phoneToSave = new Phone(phone.getNumber(), phone.getCitycode(), phone.getCountrycode(), user);
//
//                Phone phoneTmp = user.getPhones().stream()
//                        .filter(phone1 -> phoneToSave.equals(phone1))
//                        .findAny()
//                        .orElse(null);
//
//                if(phoneTmp == null){
//                    phones.add(phoneToSave);
//                }
//            }
//            phones = phoneRepository.saveAll(phones);
//
//            user.setPhones(phones);
//        }
//        return convertUserToDto(user);
//    }

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
        return userDTO;
    }

    private PhoneDTO convertPhoneToDto(Phone phone) {
        PhoneDTO phoneDTO = modelMapper.map(phone, PhoneDTO.class);
        return phoneDTO;
    }

}
