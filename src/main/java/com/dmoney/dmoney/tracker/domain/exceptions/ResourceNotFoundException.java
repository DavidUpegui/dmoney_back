package com.dmoney.dmoney.tracker.domain.exceptions;

public class ResourceNotFoundException extends DomainException{

    public ResourceNotFoundException(String resource, String field, String value){
        super("The " + resource + " with " + field + "= '" + value + "' was not found");
    }
}
