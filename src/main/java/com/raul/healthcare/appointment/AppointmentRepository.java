package com.raul.healthcare.appointment;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByDoctorId(Long doctorId);

}
