package com.healthcare.patient_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthcare.patient_service.entity.Patient;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>{

    Optional<Patient> findByAadhaarNumber(String aadharNumber);
    Optional<Patient> findByMobileNumber(String mobileNumber);


}
