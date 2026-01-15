package com.mainbat.repository;

import com.mainbat.model.equipe.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicienRepository extends JpaRepository<Technicien, Long> {
    
    List<Technicien> findByEquipeId(Long equipeId);
    
    Optional<Technicien> findByUserId(Long userId);
    
    List<Technicien> findByDisponibleTrue();
    
    @Query("SELECT t FROM Technicien t WHERE :competence MEMBER OF t.competences AND t.disponible = true")
    List<Technicien> findByCompetenceAndDisponible(@Param("competence") String competence);
    
    @Query("SELECT t FROM Technicien t WHERE t.enAstreinte = true")
    List<Technicien> findTechniciensEnAstreinte();
    
    @Query("SELECT t FROM Technicien t ORDER BY t.chargeTravail ASC")
    List<Technicien> findByChargeAsc();
}
