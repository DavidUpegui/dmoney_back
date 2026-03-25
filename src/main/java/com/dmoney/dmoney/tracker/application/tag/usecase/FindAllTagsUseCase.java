package com.dmoney.dmoney.tracker.application.tag.usecase;

import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.application.port.AuthenticatedUserProvider;
import com.dmoney.dmoney.tracker.application.tag.result.TagResponse;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllTagsUseCase {

    private final TagRepository tagRepository;
    private final AuthenticatedUserProvider authProvider;
    public List<TagResponse> execute(){

        UserId userId = authProvider.currentUserId();

        return tagRepository.findAllByUserId(userId)
                .stream()
                .map(TagResponse::from)
                .toList();
    }

}
