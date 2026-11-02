package com.dmoney.dmoney.tracker.domain.category.exceptions;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;

public class SubcategoryDoesNotBelongToCategoryException extends ValidationException {
    public SubcategoryDoesNotBelongToCategoryException(String catId, String subId){
        super("The subcategory with id: " + subId + " does not belong to the category with id: " + catId);
    }
}
