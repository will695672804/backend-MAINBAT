package com.mainbat.dto.batiment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    private String etage;
    private String description;
    private String typeZone;
    
    @NotNull(message = "Le bâtiment est obligatoire")
    private Long batimentId;
}
