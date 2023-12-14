package com.shivam.exomatter.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.shivam.exomatter.entities.Material;
import com.shivam.exomatter.models.MaterialCreateRequest;
import com.shivam.exomatter.services.MaterialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@AutoConfigureMockMvc
@SpringBootTest()
class MaterialControllerTest {

    @Autowired
    private MockMvc api;

    @MockBean
    private MaterialService materialService;


    @Test
    public void testMaterialApiRunning() throws Exception {
        api.perform(get("/material"))
                .andExpect(status().isOk());
    }

    @Test
    public void testMaterialCreateMaterialEndpointForSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Material material = Material.builder()
                .density(1.87).formula("CO2")
                .build();
        when(materialService.save(material)).thenReturn(id);

        api.perform(post("/material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                        MaterialCreateRequest.builder()
                                                .density(material.getDensity())
                                                .formula(material.getFormula())
                                                .build()
                                )
                        )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testMaterialCreateMaterialEndpointForInvalidContent() throws Exception {
        String jsonInvalidBody = """
                {
                  "formul": "CO2",
                  "density": 1.87
                }
                """;
        api.perform(post("/material")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalidBody))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testMaterialGetByValidIdEndpointForSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Material material = Material.builder().id(id)
                .density(1.87).formula("CO2")
                .build();
        when(materialService.getMaterialById(id)).thenReturn(Optional.of(material));
        api.perform(get("/material/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(material.getId().toString()))
                .andExpect(jsonPath("$.density").value(material.getDensity()))
                .andExpect(jsonPath("$.formula").value(material.getFormula()))
        ;
    }

    @Test
    public void testMaterialGetByValidUUIDIdEndpointForNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(materialService.getMaterialById(id)).thenReturn(Optional.empty());
        api.perform(get("/material/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testMaterialGetByInValidUUIDIdEndpointForSuccess() throws Exception {
        api.perform(get("/material/100"))
                .andExpect(status().isBadRequest());
    }


}
