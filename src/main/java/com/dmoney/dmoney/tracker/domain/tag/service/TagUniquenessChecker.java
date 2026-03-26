package com.dmoney.dmoney.tracker.domain.tag.service;

import com.dmoney.dmoney.shared.domain.exceptions.ResourceAlreadyExistsException;
import com.dmoney.dmoney.shared.domain.models.UserId;
import com.dmoney.dmoney.tracker.domain.tag.model.TagName;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagUniquenessChecker {
    private final TagRepository tagRepository;


    public void check(UserId userId, TagName name){
        if(tagRepository.existsByUserIdAndNameIgnoreCase(userId, name)){
            throw new ResourceAlreadyExistsException("Tag", "name", name.value());
        }
    }

    public void check(UserId userId, TagName newName, TagName currentName){
        if(!newName.value().equalsIgnoreCase(currentName.value())){
            check(userId, newName);
        }
    }
}
