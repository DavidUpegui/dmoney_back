package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementFilter;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementType;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class MovementRepositoryAdapterTest {

    @Autowired
    private MovementJpaRepository jpaRepository;

    private MovementRepositoryAdapter adapter;

    @BeforeEach
    void setUp(){
        adapter = new MovementRepositoryAdapter(jpaRepository);
    }

    private Movement newMovement(UserId userId){
        return Movement.create(
                userId,
                CategoryId.newId(),
                SubcategoryId.newId(),
                Amount.from(new BigDecimal("85.50")),
                MovementType.OUTCOME,
                MovementDescription.from("Arriendo"),
                LocalDate.of(2026, 7, 14),
                Set.of(TagId.newId())
        );
    }

    private Movement movement(UserId userId, CategoryId catId, SubcategoryId subcatId,
                              MovementType type, String description, LocalDate date, Set<TagId> tags){
        return Movement.create(
                userId,
                catId,
                subcatId,
                Amount.from(new BigDecimal("10")),
                type,
                MovementDescription.from(description),
                date,
                tags
        );
    }

    private MovementFilter emptyFilter(){
        return MovementFilter.of(null, null, null, null, null, null, null);
    }

    @Nested
    class SaveMovementRepositoryAdapterTest {
        @Test
        void should_persist_movement_and_return_mapped_domain(){
            Movement toCreate = newMovement(UserId.newId());

            Movement created = adapter.save(toCreate);

            Optional<MovementEntity> persisted =
                    jpaRepository.findById(toCreate.movementId().value());

            assertTrue(persisted.isPresent());
            assertEquals(toCreate.movementId(), created.movementId());
            assertEquals(toCreate.amount(), created.amount());
            assertEquals(toCreate.type(), created.type());
            assertEquals(toCreate.description(), created.description());
        }
    }

    @Nested
    class FindAllByUserIdMovementRepositoryAdapterTest {
        @Test
        void should_find_only_movements_belonging_to_user(){
            UserId userId = UserId.newId();
            Movement own = newMovement(userId);
            Movement foreign = newMovement(UserId.newId());

            adapter.save(own);
            adapter.save(foreign);

            List<Movement> found = adapter.findAllByUserId(userId, emptyFilter());

            assertThat(found).hasSize(1);
            assertThat(found.getFirst().movementId()).isEqualTo(own.movementId());
        }

        @Test
        void should_return_empty_list_when_no_movements_found(){
            List<Movement> found = adapter.findAllByUserId(UserId.newId(), emptyFilter());

            assertTrue(found.isEmpty());
        }
    }

    @Nested
    class FilterMovementRepositoryAdapterTest {

        private final UserId userId = UserId.newId();
        private final CategoryId food = CategoryId.newId();
        private final CategoryId transport = CategoryId.newId();
        private final SubcategoryId groceries = SubcategoryId.newId();
        private final TagId important = TagId.newId();

        private Movement groceriesMovement;
        private Movement taxiMovement;

        @BeforeEach
        void seed(){
            groceriesMovement = adapter.save(movement(
                    userId, food, groceries, MovementType.OUTCOME,
                    "Groceries at market", LocalDate.of(2026, 7, 14), Set.of(important)
            ));
            taxiMovement = adapter.save(movement(
                    userId, transport, SubcategoryId.newId(), MovementType.OUTCOME,
                    "Taxi ride", LocalDate.of(2026, 7, 20), Set.of()
            ));
        }

        @Test
        void should_filter_by_category(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(List.of(food.value().toString()), null, null, null, null, null, null));

            assertThat(found).extracting(Movement::movementId).containsExactly(groceriesMovement.movementId());
        }

        @Test
        void should_filter_by_subcategory(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, List.of(groceries.value().toString()), null, null, null, null, null));

            assertThat(found).extracting(Movement::movementId).containsExactly(groceriesMovement.movementId());
        }

        @Test
        void should_filter_by_tag(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null, List.of(important.value().toString()), null, null, null, null));

            assertThat(found).extracting(Movement::movementId).containsExactly(groceriesMovement.movementId());
        }

        @Test
        void should_filter_by_multiple_categories(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(
                            List.of(food.value().toString(), transport.value().toString()),
                            null, null, null, null, null, null));

            assertThat(found).extracting(Movement::movementId)
                    .containsExactlyInAnyOrder(groceriesMovement.movementId(), taxiMovement.movementId());
        }

        @Test
        void should_filter_by_any_of_multiple_tags(){
            TagId secondTag = TagId.newId();
            Movement second = adapter.save(movement(
                    userId, food, SubcategoryId.newId(), MovementType.OUTCOME,
                    "Second movement", LocalDate.of(2026, 7, 15), Set.of(secondTag)
            ));

            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null,
                            List.of(important.value().toString(), secondTag.value().toString()),
                            null, null, null, null));

            assertThat(found).extracting(Movement::movementId)
                    .containsExactlyInAnyOrder(groceriesMovement.movementId(), second.movementId());
        }

        @Test
        void should_filter_by_type(){
            Movement salary = adapter.save(movement(
                    userId, CategoryId.newId(), SubcategoryId.newId(), MovementType.INCOME,
                    "July salary", LocalDate.of(2026, 7, 1), Set.of()
            ));

            List<Movement> outcome = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null, null, "OUTCOME", null, null, null));
            List<Movement> income = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null, null, "INCOME", null, null, null));

            assertThat(outcome).extracting(Movement::movementId)
                    .containsExactlyInAnyOrder(groceriesMovement.movementId(), taxiMovement.movementId());
            assertThat(income).extracting(Movement::movementId)
                    .containsExactly(salary.movementId());
        }

        @Test
        void should_filter_by_date_range(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null, null, null,
                            LocalDate.of(2026, 7, 15), LocalDate.of(2026, 7, 31), null));

            assertThat(found).extracting(Movement::movementId).containsExactly(taxiMovement.movementId());
        }

        @Test
        void should_search_by_description_case_insensitively(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null, null, null, null, null, "tAxI"));

            assertThat(found).extracting(Movement::movementId).containsExactly(taxiMovement.movementId());
        }

        @Test
        void should_combine_filters_with_and(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(List.of(transport.value().toString()), null, null, "OUTCOME",
                            LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 20), null));

            assertThat(found).extracting(Movement::movementId).containsExactly(taxiMovement.movementId());
        }

        @Test
        void should_not_return_other_users_movements(){
            adapter.save(movement(
                    UserId.newId(), food, groceries, MovementType.OUTCOME,
                    "Foreign groceries", LocalDate.of(2026, 7, 14), Set.of()
            ));

            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(List.of(food.value().toString()), null, null, null, null, null, null));

            assertThat(found).extracting(Movement::movementId).containsExactly(groceriesMovement.movementId());
        }

        @Test
        void should_return_empty_when_no_match(){
            List<Movement> found = adapter.findAllByUserId(userId,
                    MovementFilter.of(null, null, null, "INCOME", null, null, null));

            assertTrue(found.isEmpty());
        }
    }

    @Nested
    class FindByUserIdAndIdMovementRepositoryAdapterTest {
        @Test
        void should_find_movement_by_id(){
            UserId userId = UserId.newId();
            Movement toFind = newMovement(userId);
            adapter.save(toFind);

            Optional<Movement> found = adapter.findByUserIdAndId(userId, toFind.movementId());

            assertThat(found).isPresent();
            assertEquals(toFind.movementId(), found.get().movementId());
        }

        @Test
        void should_return_empty_when_movement_not_found(){
            Optional<Movement> found = adapter.findByUserIdAndId(
                    UserId.newId(),
                    MovementId.newId()
            );

            assertThat(found).isNotPresent();
        }
    }

    @Nested
    class DeleteByUserIdAndIdMovementRepositoryAdapterTest {
        @Test
        void should_delete_movement_by_id(){
            UserId userId = UserId.newId();
            Movement toDelete = newMovement(userId);
            adapter.save(toDelete);

            boolean deleted = adapter.deleteByUserIdAndId(userId, toDelete.movementId());

            Optional<Movement> exists = adapter.findByUserIdAndId(userId, toDelete.movementId());

            assertThat(deleted).isTrue();
            assertThat(exists).isEmpty();
        }

        @Test
        void should_return_false_when_movement_not_deleted(){
            boolean deleted = adapter.deleteByUserIdAndId(
                    UserId.newId(),
                    MovementId.newId()
            );

            assertThat(deleted).isFalse();
        }
    }
}
