package com.dmoney.dmoney.tracker.application.category.result;

import com.dmoney.dmoney.tracker.domain.category.Subcategory;

public record SubcategoryResult(
        String categoryId,
        String subcategoryId,
        String subcategoryName,
        String subcategoryDescription
) {

    public static SubcategoryResult from(Subcategory subcategoryDomain, String categoryId){
        return new SubcategoryResult(
                categoryId,
                subcategoryDomain.id().value().toString(),
                subcategoryDomain.name().value(),
                subcategoryDomain.description().value()
        );
    }
}
