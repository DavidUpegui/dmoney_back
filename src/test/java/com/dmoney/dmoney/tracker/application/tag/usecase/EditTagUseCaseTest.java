package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.command.TagEditionCommand;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import com.dmoney.dmoney.tracker.domain.tag.service.TagUniquenessChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EditTagUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @Mock
    private TagUniquenessChecker uniquenessChecker;

    @InjectMocks
    private EditTagUseCase useCase;

    private UserId userId;
    private Tag tag;

    @BeforeEach
    void setUp(){
        UserId.newId();
        tag =  Tag.create(
                userId,
                TagName.from("Any name"),
                TagDescription.from("Any description")
        );
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_edit_tag(){
        String id = tag.id().value().toString();
        String changedName = "Changed name";
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                changedName,
                changedDescription
        );

        when(tagRepository.findByUserIdAndId(userId,tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(changedName, response.name());
        assertEquals(changedDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findByUserIdAndId(userId, tag.id());
        verify(uniquenessChecker).check(userId, TagName.from(changedName), tag.name());
    }

    @Test
    void should_throw_exception_when_tag_to_edit_not_found(){
        String anyId = UUID.randomUUID().toString();
        String changedName = "Changed name";
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                anyId,
                changedName,
                changedDescription
        );

        when(tagRepository.findByUserIdAndId(userId, TagId.from(anyId)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> useCase.execute(command));

        verify(tagRepository).findByUserIdAndId(eq(userId), any(TagId.class));
        verify(uniquenessChecker, never()).check(eq(userId), any(TagName.class));
    }

    @Test
    void should_throw_exception_when_other_tag_exists_with_the_changed_name(){
        String id = tag.id().value().toString();
        String existingName = "Existing name";
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                existingName,
                changedDescription
        );

        when(tagRepository.findByUserIdAndId(userId, tag.id()))
                .thenReturn(Optional.of(tag));

        doThrow(new ResourceAlreadyExistsException("Tag", "name", existingName))
                .when(uniquenessChecker).check(userId, TagName.from(existingName), tag.name());

        assertThrows(ResourceAlreadyExistsException.class,
                () -> useCase.execute(command));

        verify(tagRepository).findByUserIdAndId(eq(userId), any(TagId.class));
        verify(uniquenessChecker).check(userId, TagName.from(existingName), tag.name());
    }

    @Test
    void should_not_throw_exception_when_same_tag_exists_with_the_changed_name(){
        String id = tag.id().value().toString();
        String sameName = tag.name().value();
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                sameName,
                changedDescription
        );

        when(tagRepository.findByUserIdAndId(userId, tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(sameName, response.name());
        assertEquals(changedDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findByUserIdAndId(userId, tag.id());
        verify(uniquenessChecker).check(userId, TagName.from(sameName), tag.name());
    }

    @Test
    void should_edit_only_tag_name(){
        String id = tag.id().value().toString();
        String changedName = "Changed name";
        String sameDescription = tag.description().value();

        TagEditionCommand command = new TagEditionCommand(
                id,
                changedName,
                null
        );

        when(tagRepository.findByUserIdAndId(userId, tag.id()))
                .thenReturn(Optional.of(tag));


        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(changedName, response.name());
        assertEquals(sameDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findByUserIdAndId(userId, tag.id());
        verify(uniquenessChecker).check(userId, TagName.from(changedName), tag.name());
    }

    @Test
    void should_only_the_description(){
        String id = tag.id().value().toString();
        String sameName = tag.name().value();
        String changedDescription = "Changed description";

        TagEditionCommand command = new TagEditionCommand(
                id,
                null,
                changedDescription
        );

        when(tagRepository.findByUserIdAndId(userId, tag.id()))
                .thenReturn(Optional.of(tag));

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TagResponse response = useCase.execute(command);

        assertEquals(sameName, response.name());
        assertEquals(changedDescription, response.description());
        assertEquals(id, response.id());

        verify(tagRepository).findByUserIdAndId(userId, tag.id());
        verify(uniquenessChecker).check(userId, TagName.from(sameName), tag.name());
    }
}
