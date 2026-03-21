package com.dmoney.dmoney.shared.domain.exceptions;

public class DomainException extends RuntimeException {
    public DomainException(String message){
        super(message);
    }
}
