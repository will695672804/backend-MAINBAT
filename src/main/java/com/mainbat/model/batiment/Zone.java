package com.mainbat.model.batiment;

import com.mainbat.model.base.BaseEntity;
import com.mainbat.model.equipement.Equipement;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column
    private String etage;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "type_zone")
    private String typeZone;  // Salle technique, Parking, Extérieur, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batiment_id", nullable = false)
    private Batiment batiment;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Equipement> equipements = new ArrayList<>();
}
