package com.mainbat.dto.batiment;

import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.TypeBatiment;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatimentRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;
    
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;
    
    private String ville;
    private String codePostal;
    private String pays;
    private TypeBatiment typeBatiment;
    private Double surface;
    private Integer nombreEtages;
    private String description;
    private String planUrl;
    private String contactNom;
    private String contactTelephone;
    private String contactEmail;
    private NiveauCriticite criticite;
    private Double latitude;
    private Double longitude;
}
