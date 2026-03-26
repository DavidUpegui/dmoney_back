package com.dmoney.dmoney.tracker.application.category.result;

import com.dmoney.dmoney.tracker.domain.category.model.Category;

public record CategoryResult(
        String id,
        String name,
        String description,
        String type
) {

    public static CategoryResult from(Category domain){
        return new CategoryResult(
                domain.id().value().toString(),
                domain.name().value(),
                domain.description().value(),
                domain.categoryType().toString()
        );
    }
}
