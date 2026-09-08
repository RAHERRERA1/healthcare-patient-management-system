package com.raul.healthcare.exception;

import com.raul.healthcare.appointment.AppointmentNotFoundException;
import com.raul.healthcare.common.ResourceConflictException;
import com.raul.healthcare.common.ResourceNotFoundException;
import com.raul.healthcare.doctor.DoctorNotFoundException;
import com.raul.healthcare.patient.PatientNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleResourceConflictException(ResourceConflictException exception) {
        ErrorResponse errorResponse = new ErrorResponse(409, exception.getMessage());
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePatientNotFoundException(PatientNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());
        return ResponseEntity.status(404).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(400, "Validation Failed", errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppointmentNotFoundException(AppointmentNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(HttpMessageNotReadableException exception) {
        ErrorResponse errorResponse = new ErrorResponse(400, "Invalid request body");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDoctorNotFoundException(DoctorNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());
        return ResponseEntity.status(404).body(errorResponse);
    }
}
