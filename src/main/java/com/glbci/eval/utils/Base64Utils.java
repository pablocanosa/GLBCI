package com.glbci.eval.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64Utils {

    public String encode(String pwd){
        return Base64.getEncoder().encodeToString(pwd.getBytes());
    }

    public String decode(String pwd64){
        byte[] decodedBytes = Base64.getDecoder().decode(pwd64);
        return new String(decodedBytes);
    }
}
