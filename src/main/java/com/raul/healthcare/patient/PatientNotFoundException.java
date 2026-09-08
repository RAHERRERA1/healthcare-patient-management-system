package com.raul.healthcare.patient;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(Long id) {
        super("Could not find patient with id: " + id);
    }
}