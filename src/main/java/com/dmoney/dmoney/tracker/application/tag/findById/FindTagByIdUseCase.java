package com.dmoney.dmoney.tracker.application.tag.findById;

import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.Tag;
import com.dmoney.dmoney.tracker.domain.tag.TagId;
import com.dmoney.dmoney.tracker.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindTagByIdUseCase {

    private final TagRepository tagRepository;

    public TagResponse execute(String id){
        Tag founded = tagRepository.findById(TagId.from(id))
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", id));

        return TagResponse.from(founded);
    }
}
