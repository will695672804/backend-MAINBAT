package com.mainbat.dto.batiment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponse {
    
    private Long id;
    private String nom;
    private String etage;
    private String description;
    private String typeZone;
    private Long batimentId;
    private String batimentNom;
    private Long nombreEquipements;
    private LocalDateTime createdAt;
}
