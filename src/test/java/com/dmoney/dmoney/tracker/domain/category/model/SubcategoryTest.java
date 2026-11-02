package com.dmoney.dmoney.tracker.domain.category.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubcategoryTest {

    @Test
    void cat12_creates_subcategory(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        SubcategoryDescription description = SubcategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);

        assertEquals(id, subcategory.id());
        assertEquals(name, subcategory.name());
        assertEquals(description, subcategory.description());
    }

    @Test
    void shouldThrowWhenIdIsNull(){
        SubcategoryName name = SubcategoryName.from("food");
        SubcategoryDescription description = SubcategoryDescription.from("description");

        assertThrows(NullPointerException.class,
                () ->  new Subcategory(null, name, description)
        );
    }

    @Test
    void shouldThrowWhenNameIsNull(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryDescription description = SubcategoryDescription.from("description");

        assertThrows(NullPointerException.class,
                () ->  new Subcategory(id, null, description)
        );
    }

    @Test
    void cat15_renames_subcategory(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        SubcategoryDescription description = SubcategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);
        SubcategoryName newName = SubcategoryName.from("transport");
        subcategory.rename(newName);

        assertEquals(newName, subcategory.name());
    }

    @Test
    void shouldThrowWhenRenameWithValueNull(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        SubcategoryDescription description = SubcategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);

        assertThrows(NullPointerException.class,
                () ->  subcategory.rename(null)
        );
    }

    @Test
    void cat15_changes_subcategory_description(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        SubcategoryDescription description = SubcategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);
        SubcategoryDescription newDescription = SubcategoryDescription.from("newDescription");
        subcategory.changeDescription(newDescription);

        assertEquals(newDescription, subcategory.description());
    }

    @Test
    void cat22_subcategoryHasNoVisualByDefault() {
        Subcategory subcategory = new Subcategory(
                new SubcategoryId(UUID.randomUUID()),
                SubcategoryName.from("food"),
                SubcategoryDescription.from("description")
        );

        assertNull(subcategory.icon());
        assertNull(subcategory.color());
    }

    @Test
    void cat22_changesAndClearsVisual() {
        Subcategory subcategory = new Subcategory(
                new SubcategoryId(UUID.randomUUID()),
                SubcategoryName.from("food"),
                SubcategoryDescription.from("description")
        );

        subcategory.changeIcon(CategoryIcon.from("carrot"));
        subcategory.changeColor(CategoryColor.from("green"));

        assertEquals("carrot", subcategory.icon().value());
        assertEquals("green", subcategory.color().value());

        subcategory.changeIcon(null);
        subcategory.changeColor(null);

        assertNull(subcategory.icon());
        assertNull(subcategory.color());
    }

}
