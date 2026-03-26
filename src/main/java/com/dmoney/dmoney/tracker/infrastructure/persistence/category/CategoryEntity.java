package com.dmoney.dmoney.tracker.infrastructure.persistence.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="category")
@Getter
@Setter
@NoArgsConstructor
public class CategoryEntity {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "category"
    )
    private Set<SubcategoryEntity> subcategories = new HashSet<>();

    @Column(name = "type", nullable = false)
    private String type;

    public CategoryEntity(UUID id,UUID userId, String name, String description, String type){
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.type = type;
    }

}
