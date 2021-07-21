package com.a206.mychelin.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String id){
        super(id+"NotFoundException");
    }
}
