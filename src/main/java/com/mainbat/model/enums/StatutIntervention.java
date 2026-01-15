package com.mainbat.model.enums;

/**
 * Intervention status workflow
 */
public enum StatutIntervention {
    CREE,             // Created
    ASSIGNE,          // Assigned to technician
    EN_COURS,         // In progress
    EN_ATTENTE,       // Waiting for parts/supplier
    TERMINE,          // Completed
    VALIDE,           // Validated
    CLOTURE           // Closed
}
