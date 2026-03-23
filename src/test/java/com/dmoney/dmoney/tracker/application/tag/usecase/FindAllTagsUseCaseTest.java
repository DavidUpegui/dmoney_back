package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
public class FindAllTagsUseCaseTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private AuthenticatedUserProvider authProvider;

    @InjectMocks
    private FindAllTagsUseCase useCase;

    private UserId userId;

    @BeforeEach
    void setUp(){
        userId = UserId.newId();
        when(authProvider.currentUserId()).thenReturn(userId);
    }

    @Test
    void should_return_list_of_tags(){
        TagId tagId1 = TagId.newId();
        TagId tagId2 = TagId.newId();
        List<Tag> tagList = List.of(
                Tag.create(
                        userId,
                        TagName.from("Tag1 name"),
                        TagDescription.from("Tag1 Description")
                ),
                Tag.create(
                        userId,
                        TagName.from("Tag2 name"),
                        TagDescription.from("Tag2 Description")
                )
        );

        when(tagRepository.findAllByUserId(userId))
                .thenReturn(tagList);

        List<TagResponse> result = useCase.execute();


        assertEquals(tagId1.value().toString(), result.getFirst().id());
        assertEquals("Tag1 name", result.getFirst().name());
        assertEquals(tagId2.value().toString(), result.get(1).id());
        assertEquals("Tag2 name", result.get(1).name());

        verify(tagRepository).findAllByUserId(userId);
    }

    @Test
    void should_return_empty_array_if_not_found(){
        List<Tag> emptyList = List.of();

        when(tagRepository.findAllByUserId(userId))
                .thenReturn(emptyList);

        List<TagResponse> result = useCase.execute();

        assertTrue(result.isEmpty());

        verify(tagRepository).findAllByUserId(userId);
    }
}
