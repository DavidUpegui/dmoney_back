package com.dmoney.dmoney.shared.domain.exceptions;

public class ResourceInUseException extends DomainException {
    public ResourceInUseException(String resource, String detail) {
        super(resource + " is in use: " + detail);
    }
}
