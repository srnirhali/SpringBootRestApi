package com.shivam.exomatter.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shivam.exomatter.models.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name = "material_property")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialProperty {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnore
    private Material material;

    @Column(nullable = true)
    private Float calculatedValue;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
