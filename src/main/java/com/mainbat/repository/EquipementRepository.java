package com.mainbat.repository;

import com.mainbat.model.enums.EtatEquipement;
import com.mainbat.model.equipement.Equipement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    
    Page<Equipement> findByIsActiveTrue(Pageable pageable);
    
    List<Equipement> findByZoneId(Long zoneId);
    
    List<Equipement> findByEtat(EtatEquipement etat);
    
    Optional<Equipement> findByNumeroSerie(String numeroSerie);
    
    @Query("SELECT e FROM Equipement e WHERE e.zone.batiment.id = :batimentId AND e.isActive = true")
    List<Equipement> findByBatimentId(@Param("batimentId") Long batimentId);
    
    @Query("SELECT e FROM Equipement e WHERE e.etat = 'HS' OR e.etat = 'DEGRADE'")
    List<Equipement> findEquipementsEnPanne();
    
    @Query("SELECT e FROM Equipement e WHERE e.isActive = true AND " +
           "(LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.marque) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.numeroSerie) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Equipement> searchEquipements(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Equipement e WHERE e.etat = :etat")
    Long countByEtat(@Param("etat") EtatEquipement etat);
}
