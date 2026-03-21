package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.tracker.domain.category.Category;
import com.dmoney.dmoney.tracker.domain.category.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.CategoryDescription;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CategoryRepositoryAdapterTest {

    @Autowired
    private CategoryJpaRepository jpaRepository;
    private CategoryRepositoryAdapter adapter;

    @BeforeEach
    void setUp(){
        adapter = new CategoryRepositoryAdapter(jpaRepository);
    }

    @Nested
    class FindById{
        @Test
        void should_save_and_find_category(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category);

            Optional<Category> found =
                    adapter.findById(category.id());

            assertThat(found).isPresent();
            assertThat(found.get().name().value()).isEqualTo("Food");
        }

        @Test
        void should_return_empty_when_no_category_found(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category);
            CategoryId anyId = CategoryId.newId();
            Optional<Category> found =
                    adapter.findById(anyId);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    class FindAll{
        @Test
        void should_return_the_categories(){
            Category category1 = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );
            Category category2 = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Transport"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category1);
            adapter.save(category2);

            List<Category> categoryList = adapter.findAll();

            Assertions.assertThat(categoryList)
                    .hasSize(2)
                    .extracting(category -> category.name().value())
                    .containsExactlyInAnyOrder(
                            "Food",
                            "Transport"
                    );
        }

        @Test
        void should_return_void_when_no_categories(){

            List<Category> categoryList = adapter.findAll();
            assertTrue(categoryList.isEmpty());
        }
    }

    @Nested
    class Save{

        @Test
        void should_persist_category_and_return_mapped_domain() {
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            Category saved = adapter.save(category);

            assertThat(saved.id()).isEqualTo(category.id());
            assertThat(saved.name().value()).isEqualTo("Food");
            assertThat(saved.description().value())
                    .isEqualTo("Food description");

            Optional<Category> found =
                    adapter.findById(category.id());

            assertThat(found).isPresent();
        }
    }

    @Nested
    class ExistById{
        @Test
        void should_return_true_when_category_exists_by_id(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );
            CategoryId categoryId = category.id();

            adapter.save(category);

            boolean exists = adapter.existsById(categoryId);

            assertTrue(exists);
        }

        @Test
        void should_return_false_when_category_does_not_exists_by_id(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );
            CategoryId anyId = CategoryId.newId();

            adapter.save(category);

            boolean exists = adapter.existsById(anyId);

            assertFalse(exists);
        }
    }

    @Nested
    class existsByNameIgnoreCase{
        @Test
         void should_return_true_if_exists(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            adapter.save(category);

            boolean exists = adapter.existsByNameIgnoreCase(
                    CategoryName.from("Food")
            );

            assertTrue(exists);
        }

        @Test
        void should_return_true_if_exists_and_ignore_case(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            adapter.save(category);

            boolean exists = adapter.existsByNameIgnoreCase(
                    CategoryName.from("fooD")
            );

            assertTrue(exists);
        }
        @Test
        void should_return_false_if_does_not_exists(){
            Category category = new Category(
                    CategoryId.newId(),
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            adapter.save(category);

            boolean exists = adapter.existsByNameIgnoreCase(
                    CategoryName.from("Any name")
            );

            assertFalse(exists);
        }
    }

    @Nested
    class Delete{
        @Test
        void should_delete_by_id(){
            CategoryId categoryId = CategoryId.newId();
            Category category = new Category(
                    categoryId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category);

            adapter.delete(categoryId);

            boolean found = adapter.existsById(categoryId);

            assertFalse(found);
        }
    }
}
