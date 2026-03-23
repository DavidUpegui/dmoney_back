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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class TagRepositoryAdapterTest {

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
        void should_persist_tag_and_return_mapped_domain(){
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
        void should_find_only_tags_belonging_to_user() {
            UserId userId = UserId.newId();
            Tag toFound1 = Tag.create(userId, TagName.from("Name1"), TagDescription.from("Description"));
            Tag toFound2 = Tag.create(UserId.newId(), TagName.from("Name2"), TagDescription.from("Description")); // ← usuario diferente

            adapter.save(toFound1);
            adapter.save(toFound2);

            List<Tag> found = adapter.findAllByUserId(userId);

            assertThat(found.size()).isEqualTo(1);
            assertThat(found.getFirst().id()).isEqualTo(toFound1.id());
        }

        @Test
        void should_return_empty_list_when_no_tags_found(){
            UserId userId = UserId.newId();
            List<Tag> found = adapter.findAllByUserId(userId);

            assertTrue(found.isEmpty());
        }
    }

    @Nested
    class FindByIdTagRepositoryAdapterTest{
        @Test
        void should_find_a_tag_by_id(){
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
        void should_return_empty_if_tag_is_not_found(){
            TagId id = TagId.newId();
            UserId userId = UserId.newId();

            Optional<Tag> found = adapter.findByUserIdAndId(userId, id);

            assertThat(found).isNotPresent();
        }
    }

    @Nested
    class ExistByNameTagRepositoryAdapterTest{
        @Test
        void should_return_true_if_name_exist(){
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
        void should_return_false_if_name_does_not_exist(){
            TagName name =  TagName.from("Name");
            UserId userId = UserId.newId();

            boolean doExists = adapter.existsByUserIdAndNameIgnoreCase(userId,name);

            assertThat(doExists).isFalse();
        }
    }

    @Nested
    class DeleteByIdTagRepositoryAdapterTest{
        @Test
        void should_delete_tag_by_id(){
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
    }
}
