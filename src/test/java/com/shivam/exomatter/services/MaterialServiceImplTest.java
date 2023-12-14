package com.shivam.exomatter.services;

import com.shivam.exomatter.entities.Material;
import com.shivam.exomatter.entities.MaterialProperty;
import com.shivam.exomatter.repositories.MaterialPropertyRepository;
import com.shivam.exomatter.repositories.MaterialRepository;
import com.shivam.exomatter.models.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MaterialServiceImplTest {

    private MaterialService materialService;

    @MockBean
    private MaterialRepository materialRepository;

    @MockBean
    private MaterialPropertyRepository materialPropertyRepository;

    @MockBean
    private FooService fooService;

    @BeforeEach
    public void setUp() {
        materialService = new MaterialServiceImpl(materialRepository, materialPropertyRepository, fooService);
    }

    @Test
    public void calculateProperty_Success() throws InterruptedException {
        UUID id = UUID.randomUUID();
        Material material = Material.builder()
                .id(id)
                .density(1.87).formula("CO2")
                .build();
        MaterialProperty materialProperty = MaterialProperty.builder()
                .material(material).id(id)
                .status(Status.IN_PROGRESS).build();
        material.setMaterialProperty(materialProperty);
        when(materialRepository.findById(id)).thenReturn(Optional.of(material));
        when(fooService.fooness(material.getFormula())).thenReturn(10f);
        materialService.calculateProperty(id);
        assertEquals(10f, material.getMaterialProperty().getCalculatedValue());
        assertEquals(Status.SUCCESS, material.getMaterialProperty().getStatus());
    }

    @Test
    public void calculateProperty_Failure() throws InterruptedException {
        UUID id = UUID.randomUUID();
        Material material = Material.builder()
                .id(id)
                .density(1.87).formula("CO2")
                .build();
        MaterialProperty materialProperty = MaterialProperty.builder()
                .material(material).id(id)
                .status(Status.IN_PROGRESS).build();
        material.setMaterialProperty(materialProperty);
        when(materialRepository.findById(id)).thenReturn(Optional.of(material));
        when(fooService.fooness(material.getFormula())).thenThrow(InterruptedException.class);
        materialService.calculateProperty(id);
        assertNull(material.getMaterialProperty().getCalculatedValue());
        assertEquals(Status.FAILURE, material.getMaterialProperty().getStatus());
    }
}
