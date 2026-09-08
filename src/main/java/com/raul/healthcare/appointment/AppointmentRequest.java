package com.raul.healthcare.appointment;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AppointmentRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime appointmentDateTime;

    @NotBlank
    private String reason;

    @NotNull
    private AppointmentStatus status;

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() { return doctorId; }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}
