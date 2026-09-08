package com.raul.healthcare.patient;

import com.raul.healthcare.common.ResourceConflictException;
import com.raul.healthcare.common.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public List<PatientResponse> findPatients(String search) {
        List<Patient> patients = search == null || search.isBlank()
                ? patientRepository.findAll()
                : patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search);

        return patients.stream()
                .map(PatientResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PatientResponse findPatient(Long id) {
        return PatientResponse.from(getPatient(id));
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ResourceConflictException("A patient with this email already exists.");
        }

        Patient patient = new Patient();
        applyRequest(patient, request);

        return PatientResponse.from(patientRepository.save(patient));
    }

    @Transactional
    public PatientResponse updatePatient(Long id, PatientRequest request) {
        Patient patient = getPatient(id);
        patientRepository.findByEmailIgnoreCase(request.email())
                .filter(existingPatient -> !existingPatient.getId().equals(id))
                .ifPresent(existingPatient -> {
                    throw new ResourceConflictException("A patient with this email already exists.");
                });

        applyRequest(patient, request);
        patient.markUpdated();

        return PatientResponse.from(patient);
    }

    @Transactional
    public void deletePatient(Long id) {
        Patient patient = getPatient(id);
        patientRepository.delete(patient);
    }

    private Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private void applyRequest(Patient patient, PatientRequest request) {
        patient.setFirstName(request.firstName());
        patient.setLastName(request.lastName());
        patient.setDateOfBirth(request.dateOfBirth());
        patient.setGender(request.gender());
        patient.setEmail(request.email());
        patient.setPhoneNumber(request.phoneNumber());
        patient.setAddress(request.address());
    }
}
