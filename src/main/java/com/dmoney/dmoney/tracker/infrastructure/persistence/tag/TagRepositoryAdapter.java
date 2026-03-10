package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.tracker.domain.tag.Tag;
import com.dmoney.dmoney.tracker.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryAdapter implements TagRepository {

    private final TagJpaRepository tagJpaRepository;

    @Override
    public Tag create(Tag tag) {
        TagEntity toCreate = TagPersistenceMapper.toEntity(tag);
        TagEntity created = tagJpaRepository.save(toCreate);
        return TagPersistenceMapper.toDomain(created);
    }
}
