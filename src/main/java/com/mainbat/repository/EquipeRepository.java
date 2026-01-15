package com.mainbat.repository;

import com.mainbat.model.equipe.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    
    List<Equipe> findByIsActiveTrue();
}
