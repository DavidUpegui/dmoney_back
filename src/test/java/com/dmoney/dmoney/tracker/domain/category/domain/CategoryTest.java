package com.dmoney.dmoney.tracker.domain.category.domain;

import com.dmoney.dmoney.tracker.domain.category.*;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryAlreadyExistsException;
import com.dmoney.dmoney.tracker.exceptions.SubcategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryTest {

    @Test
    void shouldCreateCategoryWithoutSubcategories(){
        CategoryId id = new CategoryId(UUID.randomUUID());
        CategoryName name = CategoryName.from("name");
        Description description = Description.from("description");

        Category category = new Category(id, name, description);

        assertEquals(id, category.id());
        assertEquals(name, category.name());
        assertEquals(description, category.description());
        assertEquals(0, category.subcategories().toArray().length);
    }

    @Test
    void shouldCreateCategoryWithSubcategories(){
        Set<Subcategory> subcategories = new HashSet<>();
        Subcategory subcategory =  new Subcategory(
                SubcategoryId.newId(),
                SubcategoryName.from("subcategory name"),
                Description.from("subcategory description")
        );
        subcategories.add(subcategory);

        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description"),
                subcategories
        );

        assertEquals(1, category.subcategories().toArray().length);
    }

    @Test
    void shouldEditOnlyTheName(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );
        CategoryName newName = CategoryName.from("newName");

        category.edit(newName, null);

        assertEquals(newName, category.name());
    }

    @Test
    void shouldEditOnlyTheDescription(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );
        Description newDescription = Description.from("newDescription");

        category.edit(null, newDescription);

        assertEquals(newDescription, category.description());
    }


    @Test
    void shouldAddNewSubcategory(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        Description description = Description.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        assertEquals(1, category.subcategories().toArray().length);
        assertEquals(subcategoryName, category.subcategories().iterator().next().name());
    }

    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithNameNull() {
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );

        Description description = Description.from(("subcategoryDescription"));

        assertThrows(NullPointerException.class,
                () -> category.addSubcategory(null, description));
    }
    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithDescriptionNull() {
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");

        assertThrows(NullPointerException.class,
                () -> category.addSubcategory(subcategoryName, null));
    }

    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithANameAlreadyUsed(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        Description description = Description.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        SubcategoryName sameName = SubcategoryName.from("subcategoryName");
        Description sameDescription = Description.from(("subcategoryDescription"));

        assertThrows(SubcategoryAlreadyExistsException.class,
                () -> category.addSubcategory(subcategoryName, sameDescription));
    }

    @Test
    void shouldEditSubcategory(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                Description.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        Description description = Description.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        SubcategoryName sameName = SubcategoryName.from("subcategoryName");
        Description sameDescription = Description.from(("subcategoryDescription"));

        assertThrows(SubcategoryAlreadyExistsException.class,
                () -> category.addSubcategory(subcategoryName, sameDescription));
    }

    @Nested
    class SubcategoryOperations{

        private Subcategory subcategory;
        private Category category;

        @BeforeEach
        void setUp(){
            category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("name"),
                    Description.from("description")
            );

            subcategory = category.addSubcategory(
                    SubcategoryName.from("subcategory name"),
                    Description.from("subcategory description")
            );
        }

        @Test
        void shouldEditTheSubcategory(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            Description newSubcategoryDescription = Description.from("newDescription");

            category.editSubcategory(subcategoryId, newSubcategoryName, newSubcategoryDescription);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(newSubcategoryDescription, subcategory.description());
        }

        @Test
        void shouldEditOnlyTheSubcategoryName(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            Description oldSubcategoryDescription = subcategory.description();

            category.editSubcategory(subcategoryId, newSubcategoryName, null);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(oldSubcategoryDescription, subcategory.description());
        }

        @Test
        void shouldEditOnlyTheSubcategoryDescription(){
            SubcategoryId subcategoryId = subcategory.id();
            Description newDescription = Description.from("newDescription");
            SubcategoryName oldSubcategoryName = subcategory.name();

            category.editSubcategory(subcategoryId, null, newDescription);

            assertEquals(oldSubcategoryName, subcategory.name());
            assertEquals(newDescription, subcategory.description());
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIdIsNullInTheEdition(){
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            Description newDescription = Description.from("newDescription");


            assertThrows(NullPointerException.class,
                    () -> category.editSubcategory(null, newSubcategoryName, newDescription));
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIdWasNotFound(){
            SubcategoryId differentSubcategoryId = SubcategoryId.newId();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            Description newDescription = Description.from("newDescription");


            assertThrows(SubcategoryNotFoundException.class,
                    () -> category.editSubcategory(
                            differentSubcategoryId,
                            newSubcategoryName,
                            newDescription
                    ));
        }

        @Test
        void shouldThrowExceptionWhenThereExistASubcategoryWithSameNameInTheEdition(){
            String sameName = "sameName";
            category.addSubcategory(SubcategoryName.from(sameName), Description.empty());
            SubcategoryId subcategoryIdToEdit = subcategory.id();


            assertThrows(SubcategoryAlreadyExistsException.class,
                    () -> category.editSubcategory(
                            subcategoryIdToEdit,
                            SubcategoryName.from(sameName),
                            null));
        }

        void shouldNotThrowExceptionWhenThereExistASubcategoryWithDifferentNameInTheEdition(){
            Subcategory sc1 = subcategory; // del beforeEach

            Subcategory sc2 = category.addSubcategory(
                    SubcategoryName.from("name1"),
                    Description.empty()
            );

            Subcategory sc3 = category.addSubcategory(
                    SubcategoryName.from("name2"),
                    Description.empty()
            );

            SubcategoryName newName = SubcategoryName.from("another");

            category.editSubcategory(sc1.id(), newName, null);

            assertEquals(newName, sc1.name());
        }

        @Test
        void shouldDeleteASubcategory(){
            int initialSubcategoriesSize = category.subcategories().toArray().length;
            SubcategoryId subcategoryId = subcategory.id();

            category.deleteSubcategory(subcategoryId);

            assertEquals(initialSubcategoriesSize - 1,
                    category.subcategories().toArray().length);
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIsNotFoundWhenDeleting(){
            SubcategoryId nonExistingSubcategoryId = SubcategoryId.newId();

            assertThrows(SubcategoryNotFoundException.class,
                    () -> category.deleteSubcategory(nonExistingSubcategoryId));
        }
    }
}
