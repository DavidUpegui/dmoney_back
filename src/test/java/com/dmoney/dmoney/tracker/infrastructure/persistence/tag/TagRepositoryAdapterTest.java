package com.dmoney.dmoney.tracker.infrastructure.persistence.tag;

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
            Tag toCreate = Tag.from(
                    TagId.newId(),
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
        void should_find_a_tag_by_it_id(){
            Tag toFound1 = Tag.from(
                    TagId.newId(),
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            Tag toFound2 = Tag.from(
                    TagId.newId(),
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            adapter.save(toFound1);
            adapter.save(toFound2);

            List<Tag> found = adapter.findAll();

            assertEquals(toFound1.id(), found.getFirst().id());
            assertEquals(toFound1.name(), found.getFirst().name());
            assertEquals(toFound1.description(), found.getFirst().description());
            assertEquals(toFound2.id(), found.get(1).id());
            assertEquals(toFound2.name(), found.get(1).name());
            assertEquals(toFound2.description(), found.get(1).description());
        }

        @Test
        void should_return_empty_list_when_no_tags_found(){
            List<Tag> found = adapter.findAll();

            assertTrue(found.isEmpty());
        }
    }

    @Nested
    class FindByIdTagRepositoryAdapterTest{
        @Test
        void should_find_a_tag_by_id(){
            TagId id = TagId.newId();
            Tag toFound = Tag.from(
                    id,
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            adapter.save(toFound);

            Optional<Tag> found = adapter.findById(id);

            assertThat(found).isPresent();
            assertThat(id).isEqualTo(found.get().id());
            assertThat(toFound.name()).isEqualTo(found.get().name());
            assertThat(toFound.description()).isEqualTo(found.get().description());
        }

        @Test
        void should_return_empty_if_tag_is_not_found(){
            TagId id = TagId.newId();

            Optional<Tag> found = adapter.findById(id);

            assertThat(found).isNotPresent();
        }
    }

    @Nested
    class ExistByNameTagRepositoryAdapterTest{
        @Test
        void should_return_true_if_name_exist(){
            TagName name =  TagName.from("Name");
            Tag toFoundByName = Tag.from(
                    TagId.newId(),
                    name,
                    TagDescription.from("Description")
            );
            adapter.save(toFoundByName);

            boolean doExists = adapter.existsByName(name);

            assertThat(doExists).isTrue();
        }

        @Test
        void should_return_false_if_name_does_not_exist(){
            TagName name =  TagName.from("Name");

            boolean doExists = adapter.existsByName(name);

            assertThat(doExists).isFalse();
        }
    }

    @Nested
    class DeleteByIdTagRepositoryAdapterTest{
        @Test
        void should_delete_tag_by_id(){
            TagId id = TagId.newId();
            Tag toDelete = Tag.from(
                    id,
                    TagName.from("Name"),
                    TagDescription.from("Description")
            );
            adapter.save(toDelete);

            adapter.deleteById(id);

            boolean doExist = adapter.existsById(id);

            assertThat(doExist).isFalse();

        }
    }

}
