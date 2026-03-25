package com.dmoney.dmoney.tracker.domain.tag.repository;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    boolean deleteByUserIdAndId(UserId userId, TagId id);
    boolean existsByUserIdAndNameIgnoreCase(UserId userId, TagName tagName);
    Optional<Tag> findByUserIdAndId(UserId userId, TagId id);
    Tag save(Tag tag);
    List<Tag> findAllByUserId(UserId userId);
}
