package com.dmoney.dmoney.tracker.domain.category;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubcategoryTest {

    @Test
    void shouldCreateValidSubcategory(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        CategoryDescription description = CategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);

        assertEquals(id, subcategory.id());
        assertEquals(name, subcategory.name());
        assertEquals(description, subcategory.description());
    }

    @Test
    void shouldThrowWhenIdIsNull(){
        SubcategoryName name = SubcategoryName.from("food");
        CategoryDescription description = CategoryDescription.from("description");

        assertThrows(NullPointerException.class,
                () ->  new Subcategory(null, name, description)
        );
    }

    @Test
    void shouldThrowWhenNameIsNull(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        CategoryDescription description = CategoryDescription.from("description");

        assertThrows(NullPointerException.class,
                () ->  new Subcategory(id, null, description)
        );
    }

    @Test
    void shouldRename(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        CategoryDescription description = CategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);
        SubcategoryName newName = SubcategoryName.from("transport");
        subcategory.rename(newName);

        assertEquals(newName, subcategory.name());
    }

    @Test
    void shouldThrowWhenRenameWithValueNull(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        CategoryDescription description = CategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);

        assertThrows(NullPointerException.class,
                () ->  subcategory.rename(null)
        );
    }

    @Test
    void shouldChangeDescription(){
        SubcategoryId id = new SubcategoryId(UUID.randomUUID());
        SubcategoryName name = SubcategoryName.from("food");
        CategoryDescription description = CategoryDescription.from("description");

        Subcategory subcategory = new Subcategory(id, name, description);
        CategoryDescription newDescription = CategoryDescription.from("newDescription");
        subcategory.changeDescription(newDescription);

        assertEquals(newDescription, subcategory.description());
    }
}
