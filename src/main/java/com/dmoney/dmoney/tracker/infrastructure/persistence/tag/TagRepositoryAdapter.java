package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryAdapter implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public Tag save(Tag tag) {
        TagEntity toCreate = TagPersistenceMapper.toEntity(tag);
        TagEntity created = tagJpaRepository.save(toCreate);
        return TagPersistenceMapper.toDomain(created);
    }

    @Override
    public List<Tag> findAllByUserId(UserId userId) {
        List<TagEntity> found = tagJpaRepository.findAllByUserId(userId.value());
        return found.stream().map(TagPersistenceMapper::toDomain).toList();
    }

    @Override
    public Optional<Tag> findByUserIdAndId(UserId userId, TagId id) {
        return tagJpaRepository.findByUserIdAndId(userId.value(), id.value()).map(TagPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndId(UserId userId, TagId tagId) {
        return tagJpaRepository.existsByUserIdAndId(userId.value(), tagId.value());
    }

    @Override
    public boolean existsByUserIdAndNameIgnoreCase(UserId userId, TagName name) {
        return tagJpaRepository.existsByUserIdAndNameIgnoreCase(userId.value(), name.value());
    }

    @Override
    public boolean deleteByUserIdAndId(UserId userId, TagId id) {
        int deleted = tagJpaRepository.deleteByUserIdAndId(
                userId.value(),
                id.value()
        );

        return deleted > 0;
    }
}
