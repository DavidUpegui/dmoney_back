package com.dmoney.dmoney.tracker.application.tag.edit;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EditTagUseCase {

    private final TagRepository tagRepository;
    private final AuthenticatedUserProvider authProvider;

    public TagResponse execute(TagEditionCommand command){

        UserId userId = authProvider.currentUserId();

        Tag tagToEdit = tagRepository.findByUserIdAndId(userId, TagId.from(command.id()))
                        .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", command.id()));

        if(command.name() != null){
            TagName newName = TagName.from(command.name());
            if(!tagToEdit.name().equals(newName)
            && tagRepository.existsByUserIdAndName(userId, newName)){
                throw new ResourceAlreadyExistsException("Tag", "name", newName.value());
            }

            tagToEdit.changeName(TagName.from(command.name()));
        }
        if(command.description() != null){
            tagToEdit.changeDescription(TagDescription.fromNullable(command.description()));
        }

        return TagResponse.from(tagRepository.save(tagToEdit));
    }
}
