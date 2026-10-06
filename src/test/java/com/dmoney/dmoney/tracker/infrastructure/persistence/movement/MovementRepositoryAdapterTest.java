package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.category.model.CategoryId;
import com.dmoney.dmoney.tracker.domain.category.model.SubcategoryId;
import com.dmoney.dmoney.tracker.domain.movement.model.Amount;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementDescription;
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

            List<Movement> found = adapter.findAllByUserId(userId);

            assertThat(found).hasSize(1);
            assertThat(found.getFirst().movementId()).isEqualTo(own.movementId());
        }

        @Test
        void should_return_empty_list_when_no_movements_found(){
            List<Movement> found = adapter.findAllByUserId(UserId.newId());

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
