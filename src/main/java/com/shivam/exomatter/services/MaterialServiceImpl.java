package com.shivam.exomatter.services;

import com.shivam.exomatter.entities.Material;
import com.shivam.exomatter.entities.MaterialProperty;
import com.shivam.exomatter.repositories.MaterialPropertyRepository;
import com.shivam.exomatter.repositories.MaterialRepository;
import com.shivam.exomatter.models.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    private final MaterialPropertyRepository materialPropertyRepository;

    private final FooService fooService;

    @Override
    public List<Material> getAll() {
        return materialRepository.findAll();
    }

    @Override
    public UUID save(Material material) {
        return materialRepository.save(material).getId();
    }

    @Override
    public Optional<Material> getMaterialById(UUID materialId) {
        return materialRepository.findById(materialId);
    }

    @Override
    public List<Material> searchMaterials(Double minDensity, Double maxDensity, List<String> includes, List<String> excludes) {
        return materialRepository.search(minDensity, maxDensity, includes, excludes);
    }

    @Override
    public UUID saveMaterialProperty(MaterialProperty materialProperty) {
        return materialPropertyRepository.save(materialProperty).getId();
    }

    @Async("AsyncThreadExecutor")
    @Override
    public void calculateProperty(UUID materialId)  {
        Optional <Material> materialOptional = materialRepository.findById(materialId);
        if (materialOptional.isPresent()){
            Material material = materialOptional.get();
            try {
                material.getMaterialProperty().setCalculatedValue(fooService.fooness(material.getFormula()));
                material.getMaterialProperty().setStatus(Status.SUCCESS);
            }catch (Exception ex){
                material.getMaterialProperty().setStatus(Status.FAILURE);
            }finally {
                materialPropertyRepository.save(material.getMaterialProperty());
            }

        }
    }
}
