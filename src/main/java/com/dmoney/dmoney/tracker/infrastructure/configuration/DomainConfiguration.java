package com.dmoney.dmoney.tracker.infrastructure.configuration;

import com.dmoney.dmoney.tracker.domain.category.repository.CategoryRepository;
import com.dmoney.dmoney.tracker.domain.movement.service.MovementCategorizationValidator;
import com.dmoney.dmoney.tracker.domain.tag.repository.TagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    @Bean
    public MovementCategorizationValidator movementCategorizationValidator(
            CategoryRepository catRepo,
            TagRepository tagRepo
    ){
        return new MovementCategorizationValidator(
                catRepo,
                tagRepo
        );
    }
}
