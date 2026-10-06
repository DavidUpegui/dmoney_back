package com.dmoney.dmoney.tracker.infrastructure.persistence.movement;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.movement.model.Movement;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementFilter;
import com.dmoney.dmoney.tracker.domain.movement.model.MovementId;
import com.dmoney.dmoney.tracker.domain.movement.repository.MovementRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository jpaRepo;

    @Override
    public Movement save(Movement movement) {
        MovementEntity movementEntity = MovementPersistenceMapper.toEntity(movement);
        return MovementPersistenceMapper.toDomain(jpaRepo.save(movementEntity));
    }

    @Override
    public List<Movement> findAllByUserId(UserId userId, MovementFilter filter) {
        Specification<MovementEntity> spec = userOwned(userId).and(byFilter(filter));
        return jpaRepo.findAll(spec).stream().map(MovementPersistenceMapper::toDomain).toList();
    }

    @Override
    public Optional<Movement> findByUserIdAndId(UserId userId, MovementId id) {
        return jpaRepo.findByUserIdAndId(userId.value(), id.value())
                .map(MovementPersistenceMapper::toDomain);
    }

    @Override
    public boolean deleteByUserIdAndId(UserId userId, MovementId id) {
        int deleted = jpaRepo.deleteByUserIdAndId(userId.value(), id.value());
        return deleted > 0;
    }

    private Specification<MovementEntity> userOwned(UserId userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId.value());
    }

    private Specification<MovementEntity> byFilter(MovementFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.categories() != null && !filter.categories().isEmpty()) {
                predicates.add(root.get("catId").in(
                        filter.categories().stream().map(c -> c.value()).toList()));
            }
            if (filter.subcategories() != null && !filter.subcategories().isEmpty()) {
                predicates.add(root.get("subcatId").in(
                        filter.subcategories().stream().map(s -> s.value()).toList()));
            }
            if (filter.tags() != null && !filter.tags().isEmpty()) {
                Predicate[] tagPredicates = filter.tags().stream()
                        .map(tag -> cb.isMember(tag.value(), root.<Set<UUID>>get("tags")))
                        .toArray(Predicate[]::new);
                predicates.add(cb.or(tagPredicates));
            }
            if (filter.type() != null) {
                predicates.add(cb.equal(root.get("type"), filter.type()));
            }
            if (filter.dateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.dateFrom()));
            }
            if (filter.dateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.dateTo()));
            }
            if (filter.search() != null) {
                predicates.add(cb.like(cb.lower(root.get("description")),
                        "%" + filter.search().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
