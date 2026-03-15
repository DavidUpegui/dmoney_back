package com.dmoney.dmoney.tracker.application.tag.delete;

import com.dmoney.dmoney.tracker.domain.exceptions.ResourceNotFoundException;
import com.dmoney.dmoney.tracker.domain.tag.TagId;
import com.dmoney.dmoney.tracker.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTagUseCase {

    private final TagRepository tagRepository;

    public void execute(String id){
        TagId tagId = TagId.from(id);
        if(!tagRepository.existsById(tagId)){
            throw new ResourceNotFoundException("Tag", "id", id);
        }

        tagRepository.deleteById(tagId);
    }
}
