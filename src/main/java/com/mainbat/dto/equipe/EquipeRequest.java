package com.mainbat.dto.equipe;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipeRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String description;
    private Long superviseurId;
}
