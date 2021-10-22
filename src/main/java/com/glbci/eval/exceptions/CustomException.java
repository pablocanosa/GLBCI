package com.glbci.eval.exceptions;

import com.glbci.eval.model.ExceptionResponse;

public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
