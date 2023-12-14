package com.shivam.exomatter.services;


import com.shivam.exomatter.entities.Material;
import com.shivam.exomatter.entities.MaterialProperty;
import com.shivam.exomatter.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


public interface MaterialService {

    List<Material> getAll();

    UUID save(Material material);
    Optional<Material> getMaterialById(UUID materialId);

    List<Material> searchMaterials(Double minDensity,Double maxDensity,List<String> includes,List<String> excludes);

    UUID saveMaterialProperty(MaterialProperty materialProperty);

    void calculateProperty(UUID materialId);
}
