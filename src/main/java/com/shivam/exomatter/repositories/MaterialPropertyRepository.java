package com.shivam.exomatter.repositories;


import com.shivam.exomatter.entities.Material;
import com.shivam.exomatter.entities.MaterialProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialPropertyRepository extends JpaRepository<MaterialProperty, UUID> {

}
