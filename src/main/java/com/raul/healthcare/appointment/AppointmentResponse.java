package com.raul.healthcare.appointment;

import java.time.LocalDateTime;

public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private String patientFullName;
    private Long doctorId;
    private String doctorFullName;
    private LocalDateTime appointmentDateTime;
    private String reason;
    private AppointmentStatus status;

    public AppointmentResponse(Long id, Long patientId, String patientFullName, Long doctorId, String doctorFullName, LocalDateTime appointmentDateTime, String reason, AppointmentStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.patientFullName = patientFullName;
        this.doctorId = doctorId;
        this.doctorFullName = doctorFullName;
        this.appointmentDateTime = appointmentDateTime;
        this.reason = reason;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public Long getPatientId() {
        return patientId;
    }

    public String getPatientFullName() {
        return patientFullName;
    }

    public Long getDoctorId() {
        return doctorId;
    }
    public String getDoctorFullName() {
        return doctorFullName;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public String getReason() {
        return reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}
