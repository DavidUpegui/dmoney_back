package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.exceptions.ValidationException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldCreateCategory(){
        UserId userId = UserId.newId();
        CategoryName name = CategoryName.from("name");
        CategoryDescription description = CategoryDescription.from("description");

        Category category = Category.create(userId, name, description);

        assertNotNull(category.id());
        assertEquals(userId, category.userId());
        assertEquals(name, category.name());
        assertEquals(description, category.description());
        assertEquals(0, category.subcategories().toArray().length);
    }

    @Test
    void shouldCreateRehydrateCategory(){
        Set<Subcategory> subcategories = new HashSet<>();
        Subcategory subcategory =  new Subcategory(
                SubcategoryId.newId(),
                SubcategoryName.from("subcategory name"),
                SubcategoryDescription.from("subcategory description")
        );
        subcategories.add(subcategory);

        Category category = Category.rehydrate(
                UserId.newId(),
                CategoryId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                subcategories
        );

        assertEquals(1, category.subcategories().size());
    }

    @Test
    void shouldEditOnlyTheName(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );
        CategoryName newName = CategoryName.from("newName");

        category.edit(newName, null);

        assertEquals(newName, category.name());
    }

    @Test
    void shouldEditOnlyTheDescription(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );
        CategoryDescription newDescription = CategoryDescription.from("newDescription");

        category.edit(null, newDescription);

        assertEquals(newDescription, category.description());
    }


    @Test
    void shouldAddNewSubcategory(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        SubcategoryDescription description = SubcategoryDescription.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        assertEquals(1, category.subcategories().toArray().length);
        assertEquals(subcategoryName, category.subcategories().iterator().next().name());
    }

    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithNameNull() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );

        SubcategoryDescription description = SubcategoryDescription.from(("subcategoryDescription"));

        assertThrows(ValidationException.class,
                () -> category.addSubcategory(null, description));
    }
    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithDescriptionNull() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");

        assertThrows(ValidationException.class,
                () -> category.addSubcategory(subcategoryName, null));
    }

    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithANameAlreadyUsed(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        SubcategoryDescription description = SubcategoryDescription.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        SubcategoryDescription sameDescription = SubcategoryDescription.from(("subcategoryDescription"));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> category.addSubcategory(subcategoryName, sameDescription));
    }

    @Nested
    class SubcategoryOperations{

        private Subcategory subcategory;
        private Category category;

        @BeforeEach
        void setUp(){
            category = Category.create(
                    UserId.newId(),
                    CategoryName.from("name"),
                    CategoryDescription.from("description")
            );

            subcategory = category.addSubcategory(
                    SubcategoryName.from("subcategory name"),
                    SubcategoryDescription.from("subcategory description")
            );
        }

        @Test
        void shouldEditTheSubcategory(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            SubcategoryDescription newSubcategoryDescription = SubcategoryDescription.from("newDescription");

            category.editSubcategory(subcategoryId, newSubcategoryName, newSubcategoryDescription);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(newSubcategoryDescription, subcategory.description());
        }

        @Test
        void shouldEditOnlyTheSubcategoryName(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            SubcategoryDescription oldSubcategoryDescription = subcategory.description();

            category.editSubcategory(subcategoryId, newSubcategoryName, null);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(oldSubcategoryDescription, subcategory.description());
        }

        @Test
        void shouldEditOnlyTheSubcategoryDescription(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryDescription newDescription = SubcategoryDescription.from("newDescription");
            SubcategoryName oldSubcategoryName = subcategory.name();

            category.editSubcategory(subcategoryId, null, newDescription);

            assertEquals(oldSubcategoryName, subcategory.name());
            assertEquals(newDescription, subcategory.description());
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIdIsNullInTheEdition(){
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            SubcategoryDescription newDescription = SubcategoryDescription.from("newDescription");


            assertThrows(ValidationException.class,
                    () -> category.editSubcategory(null, newSubcategoryName, newDescription));
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIdWasNotFound(){
            SubcategoryId differentSubcategoryId = SubcategoryId.newId();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            SubcategoryDescription newDescription = SubcategoryDescription.from("newDescription");


            assertThrows(ResourceNotFoundException.class,
                    () -> category.editSubcategory(
                            differentSubcategoryId,
                            newSubcategoryName,
                            newDescription
                    ));
        }

        @Test
        void shouldThrowExceptionWhenThereExistASubcategoryWithSameNameInTheEdition(){
            String sameName = "sameName";
            category.addSubcategory(SubcategoryName.from(sameName), SubcategoryDescription.empty());
            SubcategoryId subcategoryIdToEdit = subcategory.id();



            assertThrows(ResourceAlreadyExistsException.class,
                    () -> category.editSubcategory(
                            subcategoryIdToEdit,
                            SubcategoryName.from(sameName),
                            null));
        }

        @Test
        void shouldNotThrowExceptionWhenThereExistASubcategoryWithDifferentNameInTheEdition(){
            Subcategory sc1 = subcategory;

            category.addSubcategory(
                    SubcategoryName.from("name1"),
                    SubcategoryDescription.empty()
            );

            category.addSubcategory(
                    SubcategoryName.from("name2"),
                    SubcategoryDescription.empty()
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

            assertThrows(ResourceNotFoundException.class,
                    () -> category.deleteSubcategory(nonExistingSubcategoryId));
        }
    }
}
