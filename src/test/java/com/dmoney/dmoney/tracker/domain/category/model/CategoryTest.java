package com.dmoney.dmoney.tracker.domain.category.model;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void cat01_creates_category(){
        UserId userId = UserId.newId();
        CategoryName name = CategoryName.from("name");
        CategoryDescription description = CategoryDescription.from("description");
        CategoryType type = CategoryType.INCOME;

        Category category = Category.create(userId, name, description, type);

        assertNotNull(category.id());
        assertEquals(userId, category.userId());
        assertEquals(name, category.name());
        assertEquals(description, category.description());
        assertEquals(0, category.subcategories().toArray().length);
        assertEquals(type, category.categoryType());

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
                subcategories,
                CategoryType.INCOME,
                null,
                null
        );

        assertEquals(1, category.subcategories().size());
    }

    @Test
    void cat08_changes_name(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                CategoryType.INCOME
        );
        CategoryName newName = CategoryName.from("newName");

        category.changeName(newName);

        assertEquals(newName, category.name());
    }

    @Test
    void cat08_changes_description(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                CategoryType.INCOME
        );
        CategoryDescription newDescription = CategoryDescription.from("newDescription");

        category.changeDescription(newDescription);

        assertEquals(newDescription, category.description());
    }

    @Test
    void cat08_changes_type(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                CategoryType.INCOME
        );
        CategoryType newType = CategoryType.INCOME;

        category.changeType(newType);

        assertEquals(newType, category.categoryType());
    }


    @Test
    void cat12_adds_subcategory_to_aggregate(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                CategoryType.INCOME
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
                CategoryDescription.from("description"),
                CategoryType.INCOME
        );

        SubcategoryDescription description = SubcategoryDescription.from(("subcategoryDescription"));

        assertThrows(NullPointerException.class,
                () -> category.addSubcategory(null, description));
    }
    @Test
    void shouldThrowErrorWhenAddingSubcategoryWithDescriptionNull() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                CategoryType.INCOME
        );

        SubcategoryName subcategoryName = SubcategoryName.from("subcategoryName");

        assertThrows(NullPointerException.class,
                () -> category.addSubcategory(subcategoryName, null));
    }

    @Test
    void cat13_rejects_duplicate_subcategory_name(){
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("name"),
                CategoryDescription.from("description"),
                CategoryType.INCOME
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
                    CategoryDescription.from("description"),
                    CategoryType.INCOME
            );

            subcategory = category.addSubcategory(
                    SubcategoryName.from("subcategory name"),
                    SubcategoryDescription.from("subcategory description")
            );
        }

        @Test
        void cat15_updates_subcategory_in_aggregate(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            SubcategoryDescription newSubcategoryDescription = SubcategoryDescription.from("newDescription");

            category.editSubcategory(subcategoryId, newSubcategoryName, newSubcategoryDescription);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(newSubcategoryDescription, subcategory.description());
        }

        @Test
        void cat15_edits_only_name(){
            SubcategoryId subcategoryId = subcategory.id();
            SubcategoryName newSubcategoryName = SubcategoryName.from("newName");
            SubcategoryDescription oldSubcategoryDescription = subcategory.description();

            category.editSubcategory(subcategoryId, newSubcategoryName, null);

            assertEquals(newSubcategoryName, subcategory.name());
            assertEquals(oldSubcategoryDescription, subcategory.description());
        }

        @Test
        void cat15_edits_only_description(){
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


            assertThrows(NullPointerException.class,
                    () -> category.editSubcategory(null, newSubcategoryName, newDescription));
        }

        @Test
        void cat15_rejects_missing_subcategory_in_aggregate(){
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
        void cat13_rejects_duplicate_on_rename(){
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
        void cat13_allows_rename_to_new_name(){
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
        void cat16_deletes_subcategory_in_aggregate(){
            int initialSubcategoriesSize = category.subcategories().toArray().length;
            SubcategoryId subcategoryId = subcategory.id();

            category.deleteSubcategory(subcategoryId);

            assertEquals(initialSubcategoriesSize - 1,
                    category.subcategories().toArray().length);
        }

        @Test
        void cat16_rejects_missing_subcategory_in_aggregate(){
            SubcategoryId nonExistingSubcategoryId = SubcategoryId.newId();

            assertThrows(ResourceNotFoundException.class,
                    () -> category.deleteSubcategory(nonExistingSubcategoryId));
        }
    }

    @Test
    void cat18_storesIconAndColorOnCreate() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("Food"),
                CategoryDescription.from("Food"),
                CategoryType.OUTCOME,
                CategoryIcon.from("utensils"),
                CategoryColor.from("amber")
        );

        assertEquals("utensils", category.icon().value());
        assertEquals("amber", category.color().value());
    }

    @Test
    void cat21_changesAndClearsIconAndColor() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("Food"),
                CategoryDescription.from("Food"),
                CategoryType.OUTCOME
        );

        category.changeIcon(CategoryIcon.from("utensils"));
        category.changeColor(CategoryColor.from("amber"));

        assertEquals("utensils", category.icon().value());
        assertEquals("amber", category.color().value());

        category.changeIcon(null);
        category.changeColor(null);

        assertNull(category.icon());
        assertNull(category.color());
    }

    @Test
    void cat22_addsSubcategoryWithIconAndColor() {
        Category category = Category.create(
                UserId.newId(),
                CategoryName.from("Food"),
                CategoryDescription.from("Food"),
                CategoryType.OUTCOME
        );

        Subcategory subcategory = category.addSubcategory(
                SubcategoryName.from("Groceries"),
                SubcategoryDescription.from("Groceries"),
                CategoryIcon.from("shopping-basket"),
                CategoryColor.from("green")
        );

        assertEquals("shopping-basket", subcategory.icon().value());
        assertEquals("green", subcategory.color().value());
    }

}
