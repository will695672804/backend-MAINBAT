package com.mainbat.repository;

import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.StatutIntervention;
import com.mainbat.model.enums.TypeIntervention;
import com.mainbat.model.intervention.Intervention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    
    Page<Intervention> findByIsActiveTrue(Pageable pageable);
    
    List<Intervention> findByStatut(StatutIntervention statut);
    
    List<Intervention> findByTypeIntervention(TypeIntervention type);
    
    List<Intervention> findByTechnicienId(Long technicienId);
    
    List<Intervention> findByEquipementId(Long equipementId);
    
    @Query("SELECT i FROM Intervention i WHERE i.equipement.zone.batiment.id = :batimentId")
    List<Intervention> findByBatimentId(@Param("batimentId") Long batimentId);
    
    @Query("SELECT i FROM Intervention i WHERE i.datePlanifiee BETWEEN :start AND :end AND i.isActive = true")
    List<Intervention> findByDatePlanifieeBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT i FROM Intervention i WHERE i.urgence = :urgence AND i.statut NOT IN ('TERMINE', 'CLOTURE')")
    List<Intervention> findUrgentInterventions(@Param("urgence") NiveauCriticite urgence);
    
    @Query("SELECT i FROM Intervention i WHERE i.statut NOT IN ('TERMINE', 'CLOTURE') " +
           "AND i.datePlanifiee < :now")
    List<Intervention> findInterventionsEnRetard(@Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(i) FROM Intervention i WHERE i.statut = :statut")
    Long countByStatut(@Param("statut") StatutIntervention statut);
    
    @Query("SELECT i FROM Intervention i WHERE i.technicien.id = :technicienId " +
           "AND i.statut IN ('ASSIGNE', 'EN_COURS')")
    List<Intervention> findActiveByTechnicien(@Param("technicienId") Long technicienId);
    
    // KPI Queries
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, i.dateDebut, i.dateFin)) FROM Intervention i " +
           "WHERE i.dateFin IS NOT NULL AND i.dateDebut IS NOT NULL")
    Double calculateMTTR();  // Mean Time To Repair
    
    @Query("SELECT SUM(c.montant) FROM CoutIntervention c WHERE c.intervention.equipement.zone.batiment.id = :batimentId")
    Double sumCoutsByBatiment(@Param("batimentId") Long batimentId);
}
