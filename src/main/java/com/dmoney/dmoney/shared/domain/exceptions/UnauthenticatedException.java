package com.dmoney.dmoney.shared.domain.exceptions;

public class UnauthenticatedException extends DomainException{
    public UnauthenticatedException(String message){
        super(message);
    }
}
