package com.healthcare.patient_service.service;

import com.healthcare.patient_service.entity.Patient;
import com.healthcare.patient_service.repository.PatientRepository;
import com.healthcare.patient_service.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest{

    @Mock // Creates a fake PatientRepository (no real DB needed)
    private PatientRepository repo;

    @InjectMocks  // Injects the fake repo into PatientService
    private PatientService service;

    private Patient patient;

    @BeforeEach // Runs before each test
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFullName("Ravi Kumar");
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patient.setGender("Male");
        patient.setAadhaarNumber("123456789012");
        patient.setMobileNumber("+919876543210");
        patient.setVillageOrCity("Chennai");
        patient.setDistrict("Chennai");
        patient.setState("Tamil Nadu");
        patient.setPinCode("600001");
    }

    @Test
    void registerPatient_ShouldSaveAndReturnPatient(){
        //Given tell the fake repo what to return
        when(repo.save(patient)).thenReturn(patient);

        //When call the actual service method
        Patient result = service.registerPatient(patient);

        //Then - verify the result
        assertNotNull(patient);
        assertEquals("Ravi Kumar",result.getFullName());
        verify(repo, times(1)).save(patient);//verify repo.save() was called once
    }

    @Test
    void getAllPatients_ShouldReturnList(){
        when(repo.findAll()).thenReturn(Arrays.asList(patient));

        List<Patient> result = service.getAllPatients();

        assertEquals(1,result.size());
        assertEquals("Ravi Kumar", result.get(0).getFullName());
    }

    @Test
    void getPatientById_ShouldReturnPatient(){
        when(repo.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = service.getPatientById(1l);

        assertNotNull(patient);
        assertEquals("Ravi Kumar",result.getFullName());
    }

    @Test
    void getPatientById_WhenNotFound_ShouldThrowException(){
        when(repo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->service.getPatientById(99L));

        assertEquals("Patient not found with id 99",exception.getMessage());

    }

    @Test
    void getPatientByAadhaarNumber_ShouldReturnPatient(){
        when(repo.findByAadhaarNumber("123456789012")).thenReturn(Optional.of(patient));

        Patient result = service.getPatientByAadhaarNumber("123456789012");

        assertEquals("Ravi Kumar", result.getFullName());
    }

    @Test
    void getPatientByAadhaarNumber_WhenNotFound_ShouldThrowException(){
        when(repo.findByAadhaarNumber("1232324433")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->service.getPatientByAadhaarNumber("1232324433"));

        assertEquals("Patient not found with Aadhaar 1232324433",exception.getMessage());

    }

    @Test
    void getPatientByMobileNumber_ShouldReturnPatient(){
        when(repo.findByMobileNumber("919876543210")).thenReturn(Optional.of(patient));

        Patient result = service.getPatientByMobileNumber("919876543210");

        assertEquals("Ravi Kumar", result.getFullName());
    }

    @Test
    void getPatientByMobileNumber_WhenNotFound_ShouldThrowException(){
        when(repo.findByMobileNumber("919876543456")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->service.getPatientByMobileNumber("919876543456"));

        assertEquals("Patient not found with MobileNumber 919876543456",exception.getMessage());
    }

}