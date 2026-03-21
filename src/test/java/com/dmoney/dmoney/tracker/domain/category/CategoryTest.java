package com.dmoney.dmoney.tracker.domain.category;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryTest {

    @Test
    void shouldCreateCategoryWithoutSubcategories(){
        CategoryId id = new CategoryId(UUID.randomUUID());
        CategoryName name = CategoryName.from("name");
        CategoryDescription description = CategoryDescription.from("description");

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
                CategoryDescription.from("subcategory description")
        );
        subcategories.add(subcategory);

        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                subcategories
        );

        assertEquals(1, category.subcategories().toArray().length);
    }

    @Test
    void shouldEditOnlyTheName(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
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
                CategoryDescription.from("description")
        );
        CategoryDescription newDescription = CategoryDescription.from("newDescription");

        category.edit(null, newDescription);

        assertEquals(newDescription, category.description());
    }


    @Test
    void shouldAddNewSubcategory(){
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        CategoryDescription description = CategoryDescription.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        assertEquals(1, category.subcategories().toArray().length);
        assertEquals(subcategoryName, category.subcategories().iterator().next().name());
    }

    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithNameNull() {
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
        );

        CategoryDescription description = CategoryDescription.from(("subcategoryDescription"));

        assertThrows(NullPointerException.class,
                () -> category.addSubcategory(null, description));
    }
    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithDescriptionNull() {
        Category category = new Category(
                CategoryId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description")
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
                CategoryDescription.from("description")
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");
        CategoryDescription description = CategoryDescription.from(("subcategoryDescription"));

        category.addSubcategory(subcategoryName, description);

        CategoryDescription sameDescription = CategoryDescription.from(("subcategoryDescription"));

        assertThrows(ResourceAlreadyExistsException.class,
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
                    CategoryDescription.from("description")
            );

            subcategory = category.addSubcategory(
                    SubcategoryName.from("subcategory name"),
                    CategoryDescription.from("subcategory description")
            );
        }

        @Test
        void shouldEditTheSubcategory(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            CategoryDescription newSubcategoryDescription = CategoryDescription.from("newDescription");

            category.editSubcategory(subcategoryId, newSubcategoryName, newSubcategoryDescription);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(newSubcategoryDescription, subcategory.description());
        }

        @Test
        void shouldEditOnlyTheSubcategoryName(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            CategoryDescription oldSubcategoryDescription = subcategory.description();

            category.editSubcategory(subcategoryId, newSubcategoryName, null);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(oldSubcategoryDescription, subcategory.description());
        }

        @Test
        void shouldEditOnlyTheSubcategoryDescription(){
            SubcategoryId subcategoryId = subcategory.id();
            CategoryDescription newDescription = CategoryDescription.from("newDescription");
            SubcategoryName oldSubcategoryName = subcategory.name();

            category.editSubcategory(subcategoryId, null, newDescription);

            assertEquals(oldSubcategoryName, subcategory.name());
            assertEquals(newDescription, subcategory.description());
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIdIsNullInTheEdition(){
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            CategoryDescription newDescription = CategoryDescription.from("newDescription");


            assertThrows(NullPointerException.class,
                    () -> category.editSubcategory(null, newSubcategoryName, newDescription));
        }

        @Test
        void shouldThrowExceptionWhenSubcategoryIdWasNotFound(){
            SubcategoryId differentSubcategoryId = SubcategoryId.newId();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            CategoryDescription newDescription = CategoryDescription.from("newDescription");


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
            category.addSubcategory(SubcategoryName.from(sameName), CategoryDescription.empty());
            SubcategoryId subcategoryIdToEdit = subcategory.id();



            assertThrows(ResourceAlreadyExistsException.class,
                    () -> category.editSubcategory(
                            subcategoryIdToEdit,
                            SubcategoryName.from(sameName),
                            null));
        }

        void shouldNotThrowExceptionWhenThereExistASubcategoryWithDifferentNameInTheEdition(){
            Subcategory sc1 = subcategory;

            category.addSubcategory(
                    SubcategoryName.from("name1"),
                    CategoryDescription.empty()
            );

            category.addSubcategory(
                    SubcategoryName.from("name2"),
                    CategoryDescription.empty()
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
