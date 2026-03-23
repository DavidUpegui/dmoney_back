package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.command.TagEditionCommand;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import com.dmoney.dmoney.tracker.domain.tag.service.TagUniquenessChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EditTagUseCase {

    private final TagRepository tagRepository;
    private final AuthenticatedUserProvider authProvider;
    private final TagUniquenessChecker uniquenessChecker;

    public TagResponse execute(TagEditionCommand command){

        UserId userId = authProvider.currentUserId();

        Tag tagToEdit = tagRepository.findByUserIdAndId(userId, TagId.from(command.id()))
                        .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", command.id()));

        if(command.name() != null){
            TagName newName = TagName.from(command.name());
            uniquenessChecker.check(userId, newName,tagToEdit.name());
            tagToEdit.changeName(TagName.from(command.name()));
        }
        if(command.description() != null){
            tagToEdit.changeDescription(TagDescription.fromNullable(command.description()));
        }

        return TagResponse.from(tagRepository.save(tagToEdit));
    }
}
