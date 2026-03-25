package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTagUseCase {

    private final TagRepository tagRepository;
    private final AuthenticatedUserProvider authProvider;

    public void execute(String id){
        UserId userId = authProvider.currentUserId();
        TagId tagId = TagId.from(id);
        boolean deleted = tagRepository.deleteByUserIdAndId(userId, tagId);
        if(!deleted){
            throw new ResourceNotFoundException("Tag", "id", id);
        }
    }
}
