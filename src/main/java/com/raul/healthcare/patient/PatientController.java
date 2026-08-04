package com.raul.healthcare.patient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;


@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {

        this.patientService = patientService;
    }

    @GetMapping("/api/patients/test")
    public String testPatientEndpoint() {

        return "Patient API is working";
    }

    @GetMapping("/api/patients")
    public List<PatientResponse> getPatients() {
        return patientService.getPatients();

    }

    @GetMapping("/api/patients/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        PatientResponse patientResponse = patientService.getPatientById(id);

        return ResponseEntity.ok(patientResponse);
    }

    @PostMapping("/api/patients")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientService.createPatient(patientRequest);

        return ResponseEntity.status(201).body(patientResponse);
    }

    @PutMapping("/api/patients/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientService.updatePatient(id, patientRequest);

        return ResponseEntity.ok(patientResponse);
    }

    @DeleteMapping("/api/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }
    

}