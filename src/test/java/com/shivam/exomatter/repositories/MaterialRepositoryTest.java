package com.shivam.exomatter.repositories;

import com.shivam.exomatter.entities.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MaterialRepositoryTest {

    @Autowired
    private MaterialRepository materialRepository;

    private List<Material> materials;


    @BeforeEach
    public void setUp() {
        materials = new ArrayList<>();
        for (double i = 1d; i <=3d; i++) {
            UUID id1 = UUID.randomUUID();
            Material material1 = new Material(id1,"CO2",i,null);
            materials.add(material1);
            materialRepository.save(material1);

            UUID id2 = UUID.randomUUID();
            Material material2 = new Material(id2,"Fe",i,null);
            materials.add(material2);
            materialRepository.save(material2);

            UUID id3 = UUID.randomUUID();
            Material material3 = new Material(id3,"H",i,null);
            materials.add(material3);
            materialRepository.save(material3);
        }


    }

    @AfterEach
    public void tearDown() {
        materialRepository.deleteAllById(materials.stream().map(Material::getId).toList());
        materials = null;
    }

    @Test
    public void givenMaterialIdShouldReturnMaterial(){
        Material materialToTest = materials.get(0);
        Optional<Material> retrievedMaterial = materialRepository.findById(materialToTest.getId());
        assertTrue(retrievedMaterial.isPresent());
        assertEquals(materialToTest.getId(), retrievedMaterial.get().getId());
    }

    @Test
    public void givenMaterialIdShouldBeCreated(){
        Material material = Material.builder()
                .formula("CO2")
                .density(5d).build();
        Material savedMaterial =  materialRepository.save(material);
        String validUUID = savedMaterial.getId().toString();
        assertEquals(UUID.fromString(validUUID).toString(), validUUID);
        materials.add(savedMaterial);
    }

    @Test
    void searchMaterialsOnlyMinDensity() {
        Double minDensity = 2d;
        List<Material> retrievedMaterials = materialRepository.search(minDensity,null,null, null);
        assertFalse(retrievedMaterials.isEmpty());
        List<Material> filtered = materials
                .stream()
                .filter(material ->
                        material.getDensity() >= minDensity)
                .toList();
        assertEquals(filtered,retrievedMaterials);
    }

    @Test
    void searchMaterialsOnlyMaxDensity() {
        Double maxDensity = 2d;
        List<Material> retrievedMaterials = materialRepository.search(null,maxDensity,null, null);
        assertFalse(retrievedMaterials.isEmpty());
        List<Material> filtered = materials
                .stream()
                .filter(material ->
                        material.getDensity() <= maxDensity)
                .toList();
        assertEquals(filtered,retrievedMaterials);
    }

    @Test
    void searchMaterialsIncludeCondition() {
        Double minDensity = 1d;
        Double maxDensity = 5d;
        List<String> include = List.of("CO2");
        List<Material> retrievedMaterials = materialRepository.search(minDensity,maxDensity,include, null);
        assertFalse(retrievedMaterials.isEmpty());
        List<Material> filtered = materials
                .stream()
                .filter(material -> material.getDensity() <= maxDensity &&
                        material.getDensity() >= minDensity &&
                        include.contains(material.getFormula()))
                .toList();
        assertEquals(filtered,retrievedMaterials);
    }

    @Test
    void searchMaterialsExcludeCondition() {
        Double minDensity = 1d;
        Double maxDensity = 5d;
        List<String> exclude = List.of("CO2") ;
        List<Material> retrievedMaterials = materialRepository.search(minDensity,maxDensity,null, exclude);
        assertFalse(retrievedMaterials.isEmpty());
        List<Material> filtered = materials
                .stream()
                .filter(material -> material.getDensity() <= maxDensity &&
                        material.getDensity() >= minDensity &&
                        !exclude.contains(material.getFormula()))
                .toList();
        assertEquals(filtered,retrievedMaterials);
    }


}
