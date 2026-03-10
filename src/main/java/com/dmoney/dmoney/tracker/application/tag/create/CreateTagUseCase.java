package com.dmoney.dmoney.tracker.application.tag.create;

import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.domain.tag.Tag;
import com.dmoney.dmoney.tracker.domain.tag.TagDescription;
import com.dmoney.dmoney.tracker.domain.tag.TagName;
import com.dmoney.dmoney.tracker.domain.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTagUseCase {
    private final TagRepository tagRepo;

    public TagResponse execute(CreateTagCommand command){

        Tag tag = Tag.create(
                TagName.from(command.name()),
                TagDescription.from(command.description()));

        Tag createdTag = tagRepo.create(tag);

        return TagResponse.from(tag);
    }
}
