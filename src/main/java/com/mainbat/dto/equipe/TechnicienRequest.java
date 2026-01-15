package com.mainbat.dto.equipe;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicienRequest {
    
    @NotNull(message = "L'utilisateur est obligatoire")
    private Long userId;
    
    private String matricule;
    private String telephoneUrgence;
    private Set<String> competences;
    private Set<String> certifications;
    private Set<String> zonesIntervention;
    private Long equipeId;
}
