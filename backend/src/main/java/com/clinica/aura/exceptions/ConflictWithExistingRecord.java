package com.clinica.aura.exceptions;

public class ConflictWithExistingRecord extends RuntimeException{
    public ConflictWithExistingRecord(String message) {
        super(message);
    }
}
