package com.raul.healthcare.patient;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PatientResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Gender gender,
        String email,
        String phoneNumber,
        String address,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                patient.getEmail(),
                patient.getPhoneNumber(),
                patient.getAddress(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
