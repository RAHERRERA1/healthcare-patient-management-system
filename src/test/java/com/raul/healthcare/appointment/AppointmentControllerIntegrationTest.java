package com.raul.healthcare.appointment;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.raul.healthcare.doctor.Doctor;
import com.raul.healthcare.doctor.DoctorRepository;
import com.raul.healthcare.patient.Gender;
import com.raul.healthcare.patient.Patient;
import com.raul.healthcare.patient.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        deleteTestData();
    }

    @AfterEach
    void tearDown() {
        deleteTestData();
    }

    @Test
    void createAppointmentReturnsCreatedAppointment() throws Exception {
        Patient patient = createPatient("Maria", "Garcia", "maria.garcia@example.com");
        Doctor doctor = createDoctor("Elena", "Martinez", "Cardiology", "elena.martinez@example.com");
        LocalDateTime appointmentDateTime = LocalDateTime.now().plusDays(1).withSecond(0).withNano(0);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validAppointmentJson(
                                patient.getId(),
                                doctor.getId(),
                                appointmentDateTime,
                                "Annual checkup",
                                AppointmentStatus.SCHEDULED)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.patientId").value(patient.getId()))
                .andExpect(jsonPath("$.patientFullName").value("Maria Garcia"))
                .andExpect(jsonPath("$.doctorId").value(doctor.getId()))
                .andExpect(jsonPath("$.doctorFullName").value("Elena Martinez"))
                .andExpect(jsonPath("$.appointmentDateTime").value(
                        appointmentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))))
                .andExpect(jsonPath("$.reason").value("Annual checkup"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void getAppointmentsReturnsAllAppointments() throws Exception {
        Patient patient = createPatient("Maria", "Garcia", "maria.garcia@example.com");
        Doctor doctor = createDoctor("Elena", "Martinez", "Cardiology", "elena.martinez@example.com");
        createAppointment(
                patient,
                doctor,
                LocalDateTime.now().plusDays(1),
                "Annual checkup",
                AppointmentStatus.SCHEDULED);
        createAppointment(
                patient,
                doctor,
                LocalDateTime.now().plusDays(2),
                "Follow-up visit",
                AppointmentStatus.RESCHEDULED);

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].reason", hasItems("Annual checkup", "Follow-up visit")));
    }

    private void deleteTestData() {
        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    private Patient createPatient(String firstName, String lastName, String email) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(LocalDate.of(1990, 4, 12));
        patient.setGender(Gender.FEMALE);
        patient.setEmail(email);
        patient.setPhoneNumber("555-123-4567");
        patient.setAddress("123 Main St, Dallas, TX");
        return patientRepository.save(patient);
    }

    private Doctor createDoctor(String firstName, String lastName, String specialty, String email) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialty(specialty);
        doctor.setEmail(email);
        return doctorRepository.save(doctor);
    }

    private Appointment createAppointment(
            Patient patient,
            Doctor doctor,
            LocalDateTime appointmentDateTime,
            String reason,
            AppointmentStatus status) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDateTime(appointmentDateTime);
        appointment.setReason(reason);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    private String validAppointmentJson(
            Long patientId,
            Long doctorId,
            LocalDateTime appointmentDateTime,
            String reason,
            AppointmentStatus status) {
        return """
                {
                  "patientId": %d,
                  "doctorId": %d,
                  "appointmentDateTime": "%s",
                  "reason": "%s",
                  "status": "%s"
                }
                """.formatted(patientId, doctorId, appointmentDateTime, reason, status);
    }
}
