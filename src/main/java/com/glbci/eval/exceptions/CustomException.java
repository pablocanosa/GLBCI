package com.glbci.eval.exceptions;

import com.glbci.eval.model.Error;

public class CustomException extends RuntimeException{

    private Error error;

    public CustomException(Error error){
        super(error.getMessage());
        this.error = error;
    }

    public CustomException(){

    }
}
