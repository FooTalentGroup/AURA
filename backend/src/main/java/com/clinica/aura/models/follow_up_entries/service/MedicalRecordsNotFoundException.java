package com.clinica.aura.models.follow_up_entries.service;

public class MedicalRecordsNotFoundException extends RuntimeException {
    public MedicalRecordsNotFoundException(String message) {
        super(message);
    }
}
