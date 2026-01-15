package com.mainbat.model.batiment;

import com.mainbat.model.base.BaseEntity;
import com.mainbat.model.enums.NiveauCriticite;
import com.mainbat.model.enums.TypeBatiment;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "batiments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batiment extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @Column
    private String ville;

    @Column(name = "code_postal")
    private String codePostal;

    @Column
    private String pays;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_batiment")
    private TypeBatiment typeBatiment;

    @Column
    private Double surface;  // in m²

    @Column(name = "nombre_etages")
    private Integer nombreEtages;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "plan_url")
    private String planUrl;

    @Column(name = "contact_nom")
    private String contactNom;

    @Column(name = "contact_telephone")
    private String contactTelephone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Enumerated(EnumType.STRING)
    @Column
    private NiveauCriticite criticite;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @OneToMany(mappedBy = "batiment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Zone> zones = new ArrayList<>();
}
