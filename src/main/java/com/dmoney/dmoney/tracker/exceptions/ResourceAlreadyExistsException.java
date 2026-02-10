package com.dmoney.dmoney.tracker.exceptions;

public class ResourceAlreadyExistsException extends DomainException{
    public ResourceAlreadyExistsException(String resource, String field, String value){
        super("The " + resource + " with " + field + " = '" + value + "' already exists.");
    }
}
