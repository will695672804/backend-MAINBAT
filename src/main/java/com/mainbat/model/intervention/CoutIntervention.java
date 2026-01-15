package com.mainbat.model.intervention;

import com.mainbat.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "couts_intervention")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoutIntervention extends BaseEntity {

    @Column(nullable = false)
    private String libelle;

    @Column(name = "type_cout")
    private String typeCout;  // PIECE, MAIN_OEUVRE, DEPLACEMENT, AUTRE

    @Column(nullable = false)
    private Double montant;

    @Column
    private Integer quantite;

    @Column(name = "prix_unitaire")
    private Double prixUnitaire;

    @Column
    private String fournisseur;

    @Column(name = "numero_facture")
    private String numeroFacture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intervention_id", nullable = false)
    private Intervention intervention;
}
