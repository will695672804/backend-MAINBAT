package com.mainbat.model.equipe;

import com.mainbat.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipe extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "superviseur_id")
    private Long superviseurId;

    @Column(name = "superviseur_nom")
    private String superviseurNom;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Technicien> techniciens = new ArrayList<>();
}
