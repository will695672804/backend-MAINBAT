package com.mainbat.model.notification;

import com.mainbat.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "type_notification")
    private String typeNotification;  // TICKET_ASSIGNE, STATUT_CHANGE, ALERTE_CRITIQUE, COMMENTAIRE

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "target_type")
    private String targetType;  // INTERVENTION, EQUIPEMENT, BATIMENT

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "action_url")
    private String actionUrl;
}
