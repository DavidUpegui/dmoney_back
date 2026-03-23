package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.tracker.application.tag.command.CreateTagCommand;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import com.dmoney.dmoney.tracker.domain.tag.service.TagUniquenessChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTagUseCase {

    private final TagRepository tagRepo;
    private final AuthenticatedUserProvider authProvider;
    private final TagUniquenessChecker uniquenessChecker;

    public TagResponse execute(CreateTagCommand command){

        UserId userId = authProvider.currentUserId();
        TagName tagName = TagName.from(command.name());


        uniquenessChecker.check(userId, tagName);

        Tag tag = Tag.create(
                userId,
                TagName.from(command.name()),
                TagDescription.fromNullable(command.description()));

        Tag createdTag = tagRepo.save(tag);

        return TagResponse.from(createdTag);
    }
}
