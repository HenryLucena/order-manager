package com.example.ordermanager.configuration.handler;

public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotFoundException(String e){super(e);}

    public NotFoundException(String e, Throwable throwable){super(e, throwable);}
}
