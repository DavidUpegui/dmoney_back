package com.dmoney.dmoney.tracker.application.tag.findAll;

import com.dmoney.dmoney.tracker.application.tag.TagResponse;
import com.dmoney.dmoney.tracker.domain.tag.TagRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllTagsUseCase {

    private final TagRepository tagRepository;

    public List<TagResponse> execute(){
        return tagRepository.findAll()
                .stream()
                .map(TagResponse::from)
                .toList();
    }

}
