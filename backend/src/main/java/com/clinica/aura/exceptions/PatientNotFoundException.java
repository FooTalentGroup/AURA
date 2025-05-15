package com.clinica.aura.exceptions;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(String s) {
        super(s);
    }
}
