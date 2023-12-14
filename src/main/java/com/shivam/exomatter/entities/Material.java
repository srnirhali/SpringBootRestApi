package com.shivam.exomatter.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String formula;

    @Column(nullable = false)
    private Double density;

    @OneToOne(mappedBy = "material", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder.Default
    private MaterialProperty materialProperty = null;

}
