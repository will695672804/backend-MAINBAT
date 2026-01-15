package com.mainbat.service;

import com.mainbat.model.notification.Notification;
import com.mainbat.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public Page<Notification> getNotificationsByUser(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    @Transactional(readOnly = true)
    public Long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
        log.info("Toutes les notifications marquées comme lues pour user {}", userId);
    }

    @Transactional
    public void sendNotification(Long userId, String titre, String message, 
                                  String type, String targetType, Long targetId) {
        Notification notification = Notification.builder()
                .userId(userId)
                .titre(titre)
                .message(message)
                .typeNotification(type)
                .targetType(targetType)
                .targetId(targetId)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
        log.info("Notification envoyée à user {}: {}", userId, titre);
        
        // TODO: Integrate Firebase Cloud Messaging here for push notifications
    }

    @Transactional
    public void notifyInterventionAssigned(Long technicienUserId, Long interventionId, String titre) {
        sendNotification(
                technicienUserId,
                "Nouvelle intervention assignée",
                "L'intervention \"" + titre + "\" vous a été assignée.",
                "TICKET_ASSIGNE",
                "INTERVENTION",
                interventionId
        );
    }

    @Transactional
    public void notifyInterventionStatusChanged(Long userId, Long interventionId, String newStatus) {
        sendNotification(
                userId,
                "Statut intervention modifié",
                "Le statut de l'intervention a changé: " + newStatus,
                "STATUT_CHANGE",
                "INTERVENTION",
                interventionId
        );
    }

    @Transactional
    public void notifyEquipementEnPanne(Long gestionnaireId, Long equipementId, String equipementNom) {
        sendNotification(
                gestionnaireId,
                "Équipement en panne",
                "L'équipement \"" + equipementNom + "\" est signalé en panne.",
                "ALERTE_CRITIQUE",
                "EQUIPEMENT",
                equipementId
        );
    }
}
