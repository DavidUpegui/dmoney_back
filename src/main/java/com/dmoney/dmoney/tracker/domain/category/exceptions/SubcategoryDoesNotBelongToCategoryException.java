package com.dmoney.dmoney.tracker.domain.category.exceptions;

import com.dmoney.dmoney.shared.domain.exceptions.DomainException;

public class SubcategoryDoesNotBelongToCategoryException extends DomainException {
    public SubcategoryDoesNotBelongToCategoryException(String catId, String subId){
        super("The subcategory with id: " + subId + " does not belong to the category with id: " + catId);
    }
}
