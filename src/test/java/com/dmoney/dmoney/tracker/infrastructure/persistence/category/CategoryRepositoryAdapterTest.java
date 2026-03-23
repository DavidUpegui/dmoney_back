package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.Category;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryName;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryDescription;
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
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category);

            Optional<Category> found =
                    adapter.findByUserIdAndId(userId, category.id());

            assertThat(found).isPresent();
            assertThat(found.get().name().value()).isEqualTo("Food");
        }

        @Test
        void should_return_empty_when_no_category_found(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category);
            CategoryId anyId = CategoryId.newId();
            Optional<Category> found =
                    adapter.findByUserIdAndId(userId, anyId);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    class FindAll{
        @Test
        void should_return_the_categories(){
            UserId userId = UserId.newId();
            Category category1 = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );
            Category category2 = Category.create(
                    userId,
                    CategoryName.from("Transport"),
                    CategoryDescription.from("Description")
            );

            adapter.save(category1);
            adapter.save(category2);

            List<Category> categoryList = adapter.findAllByUserId(userId);

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

            List<Category> categoryList = adapter.findAllByUserId(UserId.newId());
            assertTrue(categoryList.isEmpty());
        }
    }

    @Nested
    class Save{

        @Test
        void should_persist_category_and_return_mapped_domain() {
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            Category saved = adapter.save(category);

            assertThat(saved.id()).isEqualTo(category.id());
            assertThat(saved.name().value()).isEqualTo("Food");
            assertThat(saved.description().value())
                    .isEqualTo("Food description");

            Optional<Category> found =
                    adapter.findByUserIdAndId(userId, category.id());

            assertThat(found).isPresent();
        }
    }

    @Nested
    class existsByUserIdAndNameIgnoreCase{
        @Test
         void should_return_true_if_exists(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            adapter.save(category);

            boolean exists = adapter.existsByUserIdAndNameIgnoreCase(
                    userId,
                    CategoryName.from("Food")
            );

            assertTrue(exists);
        }

        @Test
        void should_return_true_if_exists_and_ignore_case(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            adapter.save(category);

            boolean exists = adapter.existsByUserIdAndNameIgnoreCase(
                    userId,
                    CategoryName.from("fooD")
            );

            assertTrue(exists);
        }
        @Test
        void should_return_false_if_does_not_exists(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description")
            );

            adapter.save(category);

            boolean exists = adapter.existsByUserIdAndNameIgnoreCase(
                    userId,
                    CategoryName.from("Any name")
            );

            assertFalse(exists);
        }
    }

    @Nested
    class Delete{
        @Test
        void should_delete_by_id(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description")
            );
            CategoryId categoryId = category.id();

            adapter.save(category);

            adapter.deleteByUserIdAndId(userId,categoryId);

            Optional<Category> found = adapter.findByUserIdAndId(userId,categoryId);

            assertThat(found).isEmpty();
        }
    }
}
