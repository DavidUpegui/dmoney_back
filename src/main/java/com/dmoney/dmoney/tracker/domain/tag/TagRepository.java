package com.dmoney.dmoney.tracker.domain.tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);
    List<Tag> findAll();
    Optional<Tag> findById(TagId id);
    boolean existsByName(TagName name);
    void deleteById(TagId id);
    boolean existsById(TagId id);
}
