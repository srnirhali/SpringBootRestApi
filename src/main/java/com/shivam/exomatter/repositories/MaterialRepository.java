package com.shivam.exomatter.repositories;


import com.shivam.exomatter.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {

    @Query("SELECT e FROM Material e WHERE " +
            "(:min IS NULL OR e.density >= :min) " +
            "And (:max IS NULL OR e.density <= :max) " +
            "AND (:includes IS NULL OR e.formula IN :includes) " +
            "AND (:excludes IS NULL OR e.formula NOT IN :excludes)")
    List<Material> search(Double min, Double max,List<String> includes,List<String> excludes);

}
