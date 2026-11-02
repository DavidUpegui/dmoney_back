package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

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

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void setUp(){
        adapter = new CategoryRepositoryAdapter(jpaRepository);
    }

    @Nested
    class FindById{
        @Test
        void cat01_persists_category(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description"),
                    CategoryType.INCOME
            );

            adapter.save(category);

            Optional<Category> found =
                    adapter.findByUserIdAndId(userId, category.id());

            assertThat(found).isPresent();
            assertThat(found.get().name().value()).isEqualTo("Food");
        }

        @Test
        void cat07_returns_empty(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description"),
                    CategoryType.INCOME
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
        void cat06_returns_categories_adapter(){
            UserId userId = UserId.newId();
            Category category1 = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description"),
                    CategoryType.INCOME
            );
            Category category2 = Category.create(
                    userId,
                    CategoryName.from("Transport"),
                    CategoryDescription.from("Description"),
                    CategoryType.OUTCOME
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
        void cat06_returns_empty_adapter(){

            List<Category> categoryList = adapter.findAllByUserId(UserId.newId());
            assertTrue(categoryList.isEmpty());
        }
    }

    @Nested
    class Save{

        @Test
        void cat01_persists_and_maps() {
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description"),
                    CategoryType.INCOME
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
         void cat05_detects_existing_name(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description"),
                    CategoryType.INCOME
            );

            adapter.save(category);

            boolean exists = adapter.existsByUserIdAndNameIgnoreCase(
                    userId,
                    CategoryName.from("Food")
            );

            assertTrue(exists);
        }

        @Test
        void cat05_detects_existing_name_ignoring_case(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description"),
                    CategoryType.INCOME
            );

            adapter.save(category);

            boolean exists = adapter.existsByUserIdAndNameIgnoreCase(
                    userId,
                    CategoryName.from("fooD")
            );

            assertTrue(exists);
        }
        @Test
        void cat05_reports_unknown_name(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Food description"),
                    CategoryType.INCOME
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
        void cat09_deletes_adapter(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description"),
                    CategoryType.INCOME
            );
            CategoryId categoryId = category.id();

            adapter.save(category);

            boolean deleted = adapter.deleteByUserIdAndId(userId,categoryId);

            Optional<Category> found = adapter.findByUserIdAndId(userId,categoryId);

            assertThat(deleted).isTrue();
            assertThat(found).isEmpty();
        }

        @Test
        void cat09_reports_missing_adapter(){
            UserId userId = UserId.newId();
            CategoryId anyCategoryId = CategoryId.newId();

            boolean deleted = adapter.deleteByUserIdAndId(userId,anyCategoryId);

            assertThat(deleted).isFalse();
        }
    }

    @Nested
    class DeleteCategoryCascadeTest {
        @Test
        void cat10_deleting_a_category_removes_its_subcategories(){
            UserId userId = UserId.newId();
            Category category = Category.create(
                    userId,
                    CategoryName.from("Food"),
                    CategoryDescription.from("Description"),
                    CategoryType.OUTCOME
            );
            Subcategory subcategory = category.addSubcategory(
                    SubcategoryName.from("Groceries"),
                    SubcategoryDescription.from("")
            );
            SubcategoryId subcategoryId = subcategory.id();

            adapter.save(category);

            boolean deleted = adapter.deleteByUserIdAndId(userId, category.id());
            jpaRepository.flush();

            assertThat(deleted).isTrue();
            assertThat(jdbc.queryForObject(
                    "select count(*) from subcategory where id = ?",
                    Integer.class,
                    subcategoryId.value()
            )).isZero();
        }
    }
}
