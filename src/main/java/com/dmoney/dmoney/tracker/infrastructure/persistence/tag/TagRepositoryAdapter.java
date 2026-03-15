package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.tracker.domain.tag.Tag;
import com.dmoney.dmoney.tracker.domain.tag.TagId;
import com.dmoney.dmoney.tracker.domain.tag.TagName;
import com.dmoney.dmoney.tracker.domain.tag.TagRepository;
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
    public List<Tag> findAll() {
        List<TagEntity> found = tagJpaRepository.findAll();
        return found.stream().map(TagPersistenceMapper::toDomain).toList();
    }

    @Override
    public Optional<Tag> findById(TagId id) {
        return tagJpaRepository.findById(id.value()).map(TagPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByName(TagName name) {
        return tagJpaRepository.existsByNameIgnoreCase(name.value());
    }

    @Override
    public void deleteById(TagId id) {
        tagJpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(TagId id) {
        return tagJpaRepository.existsById(id.value());
    }
}
