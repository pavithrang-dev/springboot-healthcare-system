package com.healthcare.patient_service.service;

import com.healthcare.patient_service.entity.Patient;
import com.healthcare.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PatientService {

    private final PatientRepository repo;

    public PatientService(PatientRepository repo){
        this.repo = repo;
    }

    public Patient registerPatient(Patient patient){
        return repo.save(patient);
    }

    public List<Patient> getAllPatients(){
        return repo.findAll();
    }

    public Patient getPatientById(Long id){
        return repo.findById(id).
                orElseThrow(()->new RuntimeException("Patient not found with id "+ id));
    }

    public Patient getPatientByAadhaarNumber(String aadharNo){
        return repo.findByAadhaarNumber(aadharNo).
                orElseThrow(()-> new RuntimeException("Patient not found with Aadhaar "+ aadharNo));
    }

    public Patient getPatientByMobileNumber(String mobileNo){
        return repo.findByMobileNumber(mobileNo).
                orElseThrow(()->new RuntimeException("Patient not found with MobileNumber "+ mobileNo));
    }
}
