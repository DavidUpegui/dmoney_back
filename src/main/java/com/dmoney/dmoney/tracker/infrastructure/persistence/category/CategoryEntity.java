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

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "category"
    )
    private Set<SubcategoryEntity> subcategories = new HashSet<>();

    public CategoryEntity(UUID id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
