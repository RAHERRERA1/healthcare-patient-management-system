package com.raul.healthcare.doctor;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(Long id) {
        super("Could not find doctor with id: " + id);
    }
}
