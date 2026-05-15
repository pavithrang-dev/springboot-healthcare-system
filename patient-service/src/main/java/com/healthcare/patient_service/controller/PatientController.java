package com.healthcare.patient_service.controller;
import com.healthcare.patient_service.entity.Patient;
import com.healthcare.patient_service.service.PatientService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
@RestController
@RequestMapping("api/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient){
         Patient saved = service.registerPatient(patient);
         return new ResponseEntity<>(saved,HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        return ResponseEntity.ok(service.getAllPatients());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id){
        return ResponseEntity.ok(service.getPatientById(id));
    }

    @GetMapping("/aadhaar/{aadhaar}")
    public ResponseEntity<Patient> getPatientByAadhaar(@PathVariable String aadhaar){
        return ResponseEntity.ok(service.getPatientByAadhaarNumber(aadhaar));
    }

}
