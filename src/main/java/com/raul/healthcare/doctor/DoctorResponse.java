package com.raul.healthcare.doctor;

public class DoctorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String email;

    public DoctorResponse(
            Long id,
            String firstName,
            String lastName,
            String specialty,
            String email
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
        this.email = email;
    }

    public Long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getEmail() {
        return email;
    }
}
