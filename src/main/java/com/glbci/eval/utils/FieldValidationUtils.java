package com.glbci.eval.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.glbci.eval.constants.Constants.EMAIL_REGEX;
import static com.glbci.eval.constants.Constants.PWD_REGEX;

@Component
public class FieldValidationUtils {

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePwd(String pwd) {
        Pattern pattern = Pattern.compile(PWD_REGEX);
        Matcher matcher = pattern.matcher(pwd);
        return matcher.matches();
    }
}
