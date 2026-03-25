package com.dmoney.dmoney.shared.domain.exceptions;

public class ResourceAlreadyExistsException extends DomainException{
    public ResourceAlreadyExistsException(String resource, String field, String value){
        super("The " + resource + " with " + field + " = '" + value + "' already exists.");
    }
}
