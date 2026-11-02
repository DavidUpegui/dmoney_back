package com.dmoney.dmoney.tracker.application.category.result;

import com.dmoney.dmoney.tracker.domain.category.model.Subcategory;

public record SubcategoryResult(
        String categoryId,
        String subcategoryId,
        String subcategoryName,
        String subcategoryDescription,
        String icon,
        String color
) {

    public static SubcategoryResult from(Subcategory subcategoryDomain, String categoryId){
        return new SubcategoryResult(
                categoryId,
                subcategoryDomain.id().value().toString(),
                subcategoryDomain.name().value(),
                subcategoryDomain.description().value(),
                subcategoryDomain.icon() == null ? null : subcategoryDomain.icon().value(),
                subcategoryDomain.color() == null ? null : subcategoryDomain.color().value()
        );
    }
}
