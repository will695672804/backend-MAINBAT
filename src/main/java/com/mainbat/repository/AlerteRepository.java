package com.mainbat.repository;

import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.notification.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    
    List<Alerte> findByIsResolvedFalseOrderByCreatedAtDesc();
    
    List<Alerte> findByNiveauAndIsResolvedFalse(NiveauCriticite niveau);
    
    List<Alerte> findByTargetTypeAndTargetId(String targetType, Long targetId);
    
    Long countByIsResolvedFalse();
}
