package com.mainbat.repository;

import com.mainbat.model.batiment.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    
    List<Zone> findByBatimentId(Long batimentId);
    
    List<Zone> findByBatimentIdAndIsActiveTrue(Long batimentId);
}
