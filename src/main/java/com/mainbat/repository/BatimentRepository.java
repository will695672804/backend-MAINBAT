package com.mainbat.repository;

import com.mainbat.model.batiment.Batiment;
import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.TypeBatiment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatimentRepository extends JpaRepository<Batiment, Long> {
    
    Page<Batiment> findByIsActiveTrue(Pageable pageable);
    
    List<Batiment> findByTypeBatiment(TypeBatiment type);
    
    List<Batiment> findByCriticite(NiveauCriticite criticite);
    
    @Query("SELECT b FROM Batiment b WHERE b.isActive = true AND " +
           "(LOWER(b.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.adresse) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.ville) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Batiment> searchBatiments(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Equipement e WHERE e.zone.batiment.id = :batimentId")
    Long countEquipementsByBatiment(@Param("batimentId") Long batimentId);
}
