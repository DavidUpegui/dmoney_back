package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.shared.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.model.Tag;
import com.dmoney.dmoney.tracker.domain.tag.model.TagId;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindTagByIdUseCase {

    private final TagRepository tagRepository;
    private final AuthenticatedUserProvider authProvider;

    public TagResponse execute(String id){

        UserId userId = authProvider.currentUserId();

        Tag founded = tagRepository.findByUserIdAndId(userId,TagId.from(id))
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));

        return TagResponse.from(founded);
    }
}
