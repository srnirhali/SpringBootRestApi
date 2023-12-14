package com.shivam.exomatter.controller;

import com.shivam.exomatter.entities.Material;
import com.shivam.exomatter.entities.MaterialProperty;
import com.shivam.exomatter.models.MaterialCreateRequest;
import com.shivam.exomatter.models.Status;
import com.shivam.exomatter.services.MaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/material")
@Validated
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;


    @GetMapping()
    ResponseEntity<List<Material>> all() {
        return  ResponseEntity.ok(materialService.getAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Material> getMaterialById(@PathVariable UUID id) {
        return materialService.getMaterialById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/calculate_property")
    ResponseEntity<Map<String,String>> calculateProperty(@PathVariable UUID id) {
         var material= materialService.getMaterialById(id);
         if(material.isPresent()){
             Material m = material.get();
             MaterialProperty materialProperty = m.getMaterialProperty();
             if(materialProperty==null) {
                 materialProperty = MaterialProperty.builder()
                         .material(m)
                         .status(Status.IN_PROGRESS)
                         .build();

             }else {
                 materialProperty.setCalculatedValue(null);
                 materialProperty.setStatus(Status.IN_PROGRESS);
             }
             UUID propertyId = materialService.saveMaterialProperty(materialProperty);
             materialService.calculateProperty(m.getId());
             return new ResponseEntity<>(Map.of("id",propertyId.toString()), HttpStatus.CREATED);
         }
         return ResponseEntity.notFound().build();

    }


    @GetMapping("/search")
    ResponseEntity<List<Material>> searchMaterials(@RequestParam(value = "min-density",required = false) Double minDensity,
                                                    @RequestParam(value = "max-density",required = false) Double maxDensity,
                                                   @RequestParam(value = "include-elements",required = false) List<String> includes,
                                                   @RequestParam(value = "exclude-elements",required = false) List<String> excludes) {
        if ((minDensity!=null && maxDensity!= null) && minDensity > maxDensity){
            throw new IllegalArgumentException("min-density should not be greater than max-density");
        }
        return ResponseEntity.ok(materialService.searchMaterials(minDensity,maxDensity,includes,excludes));
    }


    @PostMapping()
    ResponseEntity<Map<String,String>> createMaterial(@Valid @RequestBody MaterialCreateRequest materialCreateRequest){
        Material m = new Material();
        m.setFormula(materialCreateRequest.getFormula());
        m.setDensity(materialCreateRequest.getDensity());
        return new ResponseEntity<>( Map.of("id",materialService.save(m).toString()),HttpStatus.CREATED);
    }


}
