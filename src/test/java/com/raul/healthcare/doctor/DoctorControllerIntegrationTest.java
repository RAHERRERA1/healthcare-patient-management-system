package com.raul.healthcare.doctor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
class DoctorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorRepository doctorRepository;

    @BeforeEach
    void setUp() {
        doctorRepository.deleteAll();
    }

    @Test
    void createDoctorReturnsCreatedDoctor() throws Exception {
        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validDoctorJson("Elena", "Martinez", "Cardiology", "elena.martinez@example.com")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Elena"))
                .andExpect(jsonPath("$.lastName").value("Martinez"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"))
                .andExpect(jsonPath("$.email").value("elena.martinez@example.com"));

    }

    @Test
    void getDoctorsReturnsAllDoctors() throws Exception {
        createDoctor("Elena", "Martinez", "Cardiology", "elena.martinez@example.com");
        createDoctor("James", "Wilson", "Pediatrics", "james.wilson@example.com");
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getDoctorByIdReturnsDoctor() throws Exception {
        Doctor doctor = createDoctor("Elena", "Martinez", "Cardiology", "elena.martinez@example.com");
        mockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.id").value(doctor.getId()))
                .andExpect(jsonPath("$.firstName").value("Elena"))
                .andExpect(jsonPath("$.email").value("elena.martinez@example.com"));
    }

    @Test
    void getDoctorByIdReturnsNotFoundWhenDoctorDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/doctors/{id}",1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Could not find doctor with id: 1"));
    }

    private Doctor createDoctor(String firstName, String lastName, String specialty, String email) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setSpecialty(specialty);
        doctor.setEmail(email);
        return doctorRepository.save(doctor);
    }


    private String validDoctorJson(String firstName, String lastName, String specialty, String email) {
        return """
                {
                "firstName": "%s",
                "lastName": "%s",
                "specialty": "%s",
                "email": "%s"
                }
                """.formatted(firstName, lastName, specialty, email);
    }
}