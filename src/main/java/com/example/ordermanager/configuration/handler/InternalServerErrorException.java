package com.example.ordermanager.configuration.handler;

public class InternalServerErrorException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String exception){
        super(exception);
    }

    public InternalServerErrorException(String exception, Throwable throwable){
        super(exception, throwable);
    }
}
