package com.raul.healthcare.patient;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
    }

    @Test
    void createPatientReturnsCreatedPatient() throws Exception {
        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPatientJson("Maria", "Garcia", "maria.garcia@example.com")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/patients/")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName").value("Maria"))
                .andExpect(jsonPath("$.lastName").value("Garcia"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-04-12"))
                .andExpect(jsonPath("$.gender").value("FEMALE"))
                .andExpect(jsonPath("$.email").value("maria.garcia@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("555-123-4567"))
                .andExpect(jsonPath("$.address").value("123 Main St, Dallas, TX"));
    }

    @Test
    void findPatientsReturnsAllPatientsWhenSearchIsBlank() throws Exception {
        createPatient("Maria", "Garcia", "maria.garcia@example.com");
        createPatient("James", "Wilson", "james.wilson@example.com");

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void findPatientsCanSearchByFirstOrLastName() throws Exception {
        createPatient("Maria", "Garcia", "maria.garcia@example.com");
        createPatient("James", "Wilson", "james.wilson@example.com");

        mockMvc.perform(get("/api/patients").param("search", "gar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Maria"))
                .andExpect(jsonPath("$[0].lastName").value("Garcia"));
    }

    @Test
    void searchPatientGetsByLastName() throws Exception {
        createPatient("Maria", "Garcia", "maria.garcia@example.com");

        mockMvc.perform(get("/api/patients").param("search", "Garcia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].lastName").value("Garcia"));
    }

    @Test
    void searchPatientGetsByFirstName() throws Exception {
        createPatient("Raul", "Romero", "raulromero@example.com");

        mockMvc.perform(get("/api/patients").param("search", "Raul"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Raul"));

    }

    @Test
    void searchPatientReturnsEmptyListWhenNoPatientsMatch() throws Exception {
        createPatient("Maria", "Garcia", "maria.garcia@example.com");
        mockMvc.perform(get("/api/patients").param("search", "NoMatch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void findPatientReturnsNotFoundWhenPatientDoesNotExist() throws Exception {
        createPatient("Maria", "Garcia", "maria.garcia@example.com");
        mockMvc.perform(get("/api/patients/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Patient not found with id: 999"));
    }

    @Test
    void findPatientReturnsPatientById() throws Exception {
        Patient patient = createPatient("Maria", "Garcia", "maria.garcia@example.com");

        mockMvc.perform(get("/api/patients/{id}", patient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patient.getId()))
                .andExpect(jsonPath("$.email").value("maria.garcia@example.com"));
    }

    @Test
    void updatePatientReturnsNotFoundWhenPatientDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/patients/{id}", 999)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validPatientJson("Maria", "Garcia", "maria.garcia@example.com")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Patient not found with id: 999"));

    }

    @Test
    void updatePatientReturnsUpdatedPatient() throws Exception {
        Patient patient = createPatient("Maria", "Garcia", "maria.garcia@example.com");

        mockMvc.perform(put("/api/patients/{id}", patient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPatientJson("Marisol", "Garcia", "marisol.garcia@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patient.getId()))
                .andExpect(jsonPath("$.firstName").value("Marisol"))
                .andExpect(jsonPath("$.email").value("marisol.garcia@example.com"));
    }

    @Test
    void deletePatientRemovesPatient() throws Exception {
        Patient patient = createPatient("Maria", "Garcia", "maria.garcia@example.com");

        mockMvc.perform(delete("/api/patients/{id}", patient.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/patients/{id}", patient.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deletePatientReturnsNotFoundWhenPatientDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/patients/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Patient not found with id: 999"));
    }

    @Test
    void createPatientReturnsBadRequestForInvalidRequest() throws Exception {
        String invalidJson = """
                {
                  "firstName": "",
                  "lastName": "",
                  "dateOfBirth": "2035-01-01",
                  "gender": null,
                  "email": "not-an-email",
                  "phoneNumber": "",
                  "address": "123 Main St, Dallas, TX"
                }
                """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.lastName").exists())
                .andExpect(jsonPath("$.errors.dateOfBirth").exists())
                .andExpect(jsonPath("$.errors.gender").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.phoneNumber").exists());
    }

    @Test
    void createPatientReturnsConflictForDuplicateEmail() throws Exception {
        createPatient("Maria", "Garcia", "maria.garcia@example.com");

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPatientJson("Marie", "Gomez", "MARIA.GARCIA@example.com")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("A patient with this email already exists."));
    }

    private Patient createPatient(String firstName, String lastName, String email) {
        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setDateOfBirth(java.time.LocalDate.of(1990, 4, 12));
        patient.setGender(Gender.FEMALE);
        patient.setEmail(email);
        patient.setPhoneNumber("555-123-4567");
        patient.setAddress("123 Main St, Dallas, TX");
        return patientRepository.save(patient);
    }

    private String validPatientJson(String firstName, String lastName, String email) {
        return """
                {
                  "firstName": "%s",
                  "lastName": "%s",
                  "dateOfBirth": "1990-04-12",
                  "gender": "FEMALE",
                  "email": "%s",
                  "phoneNumber": "555-123-4567",
                  "address": "123 Main St, Dallas, TX"
                }
                """.formatted(firstName, lastName, email);
    }
}
