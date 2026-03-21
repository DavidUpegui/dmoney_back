package com.dmoney.dmoney.shared.domain.exceptions;

public class UnauthenticatedException extends DomainException{
    public UnauthenticatedException(){
        super("User not authenticated");
    }
}
