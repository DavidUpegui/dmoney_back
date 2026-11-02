package com.dmoney.dmoney.tracker.domain.movement.model;

import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementFilterTest {

    @Test
    void mov18_returns_empty_filter(){
        MovementFilter filter = MovementFilter.of(null, null, null, null, null, null, null);

        assertTrue(filter.categories().isEmpty());
        assertTrue(filter.subcategories().isEmpty());
        assertTrue(filter.tags().isEmpty());
        assertNull(filter.type());
        assertNull(filter.dateFrom());
        assertNull(filter.dateTo());
        assertNull(filter.search());
    }

    @Test
    void mov12_parses_multiple_categories(){
        String categoryId1 = CategoryId.newId().value().toString();
        String categoryId2 = CategoryId.newId().value().toString();
        String subcategoryId = SubcategoryId.newId().value().toString();
        String tagId1 = TagId.newId().value().toString();
        String tagId2 = TagId.newId().value().toString();

        MovementFilter filter = MovementFilter.of(
                List.of(categoryId1, categoryId2),
                List.of(subcategoryId),
                List.of(tagId1, tagId2),
                "income",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31),
                "  salary  "
        );

        assertEquals(2, filter.categories().size());
        assertEquals(1, filter.subcategories().size());
        assertEquals(2, filter.tags().size());
        assertEquals(MovementType.INCOME, filter.type());
        assertEquals(LocalDate.of(2026, 1, 1), filter.dateFrom());
        assertEquals(LocalDate.of(2026, 1, 31), filter.dateTo());
        assertEquals("salary", filter.search());
    }

    @Test
    void mov17_ignores_blank_search(){
        MovementFilter filter = MovementFilter.of(null, null, null, null, null, null, "   ");

        assertNull(filter.search());
    }

    @Test
    void mov19_rejects_malformed_category(){
        assertThrows(ValidationException.class,
                () -> MovementFilter.of(List.of("not-a-uuid"), null, null, null, null, null, null));
    }

    @Test
    void mov19_rejects_malformed_subcategory(){
        assertThrows(ValidationException.class,
                () -> MovementFilter.of(null, List.of("not-a-uuid"), null, null, null, null, null));
    }

    @Test
    void mov19_rejects_malformed_tag(){
        assertThrows(ValidationException.class,
                () -> MovementFilter.of(null, null, List.of("not-a-uuid"), null, null, null, null));
    }

    @Test
    void mov19_rejects_invalid_type(){
        assertThrows(ValidationException.class,
                () -> MovementFilter.of(null, null, null, "FOO", null, null, null));
    }
}
