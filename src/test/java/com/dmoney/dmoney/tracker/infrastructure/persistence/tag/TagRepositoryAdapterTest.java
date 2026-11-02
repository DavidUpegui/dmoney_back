package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TagRepositoryAdapterTest {

    @Autowired
    private TagJpaRepository jpaRepository;

    private TagRepositoryAdapter adapter;

    @BeforeEach
    void setUp(){
        adapter = new TagRepositoryAdapter(jpaRepository);
    }

    @Nested
    class CreateTagRepositoryAdapterTest{
        @Test
        void tag01_persists_tag(){
            Tag toCreate = Tag.create(
                    UserId.newId(),
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );

            Tag created = adapter.save(toCreate);

            Optional<TagEntity> persisted =
                    jpaRepository.findById(toCreate.id().value());

            assertTrue(persisted.isPresent());
            assertEquals(toCreate.id(), created.id());
            assertEquals(toCreate.name(), created.name());
            assertEquals(toCreate.description(), created.description());
        }
    }

    @Nested
    class FindAllTagRepositoryAdapterTest{
        @Test
        void tag05_finds_only_own_tags() {
            UserId userId = UserId.newId();
            Tag toFound1 = Tag.create(userId, TagName.from("Name1"), TagDescription.from("Description"));
            Tag toFound2 = Tag.create(UserId.newId(), TagName.from("Name2"), TagDescription.from("Description")); // ← usuario diferente

            adapter.save(toFound1);
            adapter.save(toFound2);

            List<Tag> found = adapter.findAllByUserId(userId);

            assertThat(found).hasSize(1);
            assertThat(found.getFirst().id()).isEqualTo(toFound1.id());
        }

        @Test
        void tag06_returns_empty_list_for_user(){
            UserId userId = UserId.newId();
            List<Tag> found = adapter.findAllByUserId(userId);

            assertTrue(found.isEmpty());
        }
    }

    @Nested
    class FindByIdTagRepositoryAdapterTest{
        @Test
        void tag07_finds_tag_by_id(){
            UserId userId = UserId.newId();
            Tag toFound = Tag.create(
                    userId,
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            TagId id = toFound.id();
            adapter.save(toFound);

            Optional<Tag> found = adapter.findByUserIdAndId(userId, id);

            assertThat(found).isPresent();
            assertThat(id).isEqualTo(found.get().id());
            assertThat(toFound.name()).isEqualTo(found.get().name());
            assertThat(toFound.description()).isEqualTo(found.get().description());
        }

        @Test
        void tag07_returns_empty_for_unknown_id(){
            TagId id = TagId.newId();
            UserId userId = UserId.newId();

            Optional<Tag> found = adapter.findByUserIdAndId(userId, id);

            assertThat(found).isNotPresent();
        }
    }

    @Nested
    class ExistByNameTagRepositoryAdapterTest{
        @Test
        void tag04_detects_existing_name(){
            UserId userId = UserId.newId();
            TagName name =  TagName.from("Name");
            Tag toFoundByName = Tag.create(
                    userId,
                    name,
                    TagDescription.from("Description")
            );
            adapter.save(toFoundByName);

            boolean doExists = adapter.existsByUserIdAndNameIgnoreCase(userId, name);

            assertThat(doExists).isTrue();
        }

        @Test
        void tag04_detects_existing_name_ignoring_case(){
            UserId userId = UserId.newId();
            adapter.save(Tag.create(
                    userId,
                    TagName.from("Vacation"),
                    TagDescription.from("Description")
            ));

            boolean doExists = adapter.existsByUserIdAndNameIgnoreCase(
                    userId,
                    TagName.from("VACATION")
            );

            assertThat(doExists).isTrue();
        }

        @Test
        void tag04_reports_unknown_name(){
            TagName name =  TagName.from("Name");
            UserId userId = UserId.newId();

            boolean doExists = adapter.existsByUserIdAndNameIgnoreCase(userId,name);

            assertThat(doExists).isFalse();
        }
    }

    @Nested
    class DeleteByIdTagRepositoryAdapterTest{
        @Test
        void tag11_deletes_own_tag(){
            UserId userId = UserId.newId();
            Tag toDelete = Tag.create(
                    userId,
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            TagId id = toDelete.id();
            adapter.save(toDelete);

            boolean deleted = adapter.deleteByUserIdAndId(userId,id);

            Optional<Tag> doExist = adapter.findByUserIdAndId(userId, id);

            assertThat(deleted).isTrue();
            assertThat(doExist).isEmpty();
        }

        @Test
        void tag11_reports_missing_tag(){
            UserId userId = UserId.newId();
            TagId anyTagId = TagId.newId();

            boolean deleted = adapter.deleteByUserIdAndId(userId,anyTagId);


            assertThat(deleted).isFalse();
        }
    }

    @Nested
    class OwnerScopingTagRepositoryAdapterTest{
        @Test
        void tag05_does_not_expose_or_delete_another_users_tag(){
            UserId owner = UserId.newId();
            UserId other = UserId.newId();
            Tag tag = Tag.create(
                    owner,
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            adapter.save(tag);

            assertThat(adapter.findByUserIdAndId(other, tag.id())).isEmpty();
            assertThat(adapter.existsByUserIdAndId(other, tag.id())).isFalse();
            assertThat(adapter.deleteByUserIdAndId(other, tag.id())).isFalse();
            assertThat(adapter.findByUserIdAndId(owner, tag.id())).isPresent();
        }
    }
}
