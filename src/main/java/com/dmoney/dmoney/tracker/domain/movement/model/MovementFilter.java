package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record MovementFilter(
        Set<CategoryId> categories,
        Set<SubcategoryId> subcategories,
        Set<TagId> tags,
        MovementType type,
        LocalDate dateFrom,
        LocalDate dateTo,
        String search
) {

    public static MovementFilter of(
            List<String> categoryIds,
            List<String> subcategoryIds,
            List<String> tagIds,
            String type,
            LocalDate dateFrom,
            LocalDate dateTo,
            String search
    ){
        return new MovementFilter(
                parseCategories(categoryIds),
                parseSubcategories(subcategoryIds),
                parseTags(tagIds),
                type == null ? null : MovementType.from(type),
                dateFrom,
                dateTo,
                normalizeSearch(search)
        );
    }

    private static Set<CategoryId> parseCategories(List<String> values){
        if (values == null) {
            return Set.of();
        }
        return values.stream()
                .map(MovementFilter::parseCategoryId)
                .collect(Collectors.toSet());
    }

    private static Set<SubcategoryId> parseSubcategories(List<String> values){
        if (values == null) {
            return Set.of();
        }
        return values.stream()
                .map(MovementFilter::parseSubcategoryId)
                .collect(Collectors.toSet());
    }

    private static Set<TagId> parseTags(List<String> values){
        if (values == null) {
            return Set.of();
        }
        return values.stream()
                .map(MovementFilter::parseTagId)
                .collect(Collectors.toSet());
    }

    private static CategoryId parseCategoryId(String value){
        return CategoryId.from(value);
    }

    private static SubcategoryId parseSubcategoryId(String value){
        return SubcategoryId.from(value);
    }

    private static TagId parseTagId(String value){
        return TagId.from(value);
    }

    private static String normalizeSearch(String search){
        if (search == null) {
            return null;
        }
        String trimmed = search.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
