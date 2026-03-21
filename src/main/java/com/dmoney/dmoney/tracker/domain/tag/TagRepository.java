package com.dmoney.dmoney.tracker.domain.tag;

import com.dmoney.dmoney.shared.domain.models.UserId;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    boolean deleteByUserIdAndId(UserId userId, TagId id);
    boolean existsByUserIdAndName(UserId userId, TagName tagName);
    Optional<Tag> findByUserIdAndId(UserId userId, TagId id);
    Tag save(Tag tag);
    List<Tag> findAllByUserId(UserId userId);
}
