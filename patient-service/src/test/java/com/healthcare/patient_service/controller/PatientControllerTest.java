package com.healthcare.patient_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.healthcare.patient_service.controller.PatientController;
import com.healthcare.patient_service.entity.Patient;
import com.healthcare.patient_service.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)   //only loads the controller layaer
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean  // Fake service injected into the controller
    private PatientService service;

    private Patient patient;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
         objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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
    void registerPatient_ShouldReturn201() throws Exception {
    when(service.registerPatient(any(Patient.class))).thenReturn(patient);

    mockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(patient)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.fullName").value("Ravi Kumar"));

    }

    @Test
    void getAllPatients_ShouldReturn200()throws Exception{
        when(service.getAllPatients()).thenReturn(Arrays.asList(patient));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fullName").value("Ravi Kumar"));
    }
    @Test
    void getPatientById_ShouldReturn200() throws Exception {
        when(service.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ravi Kumar"));
    }

    @Test
    void getPatientByAadhaar_ShouldReturn200() throws Exception {
        when(service.getPatientByAadhaarNumber("123456789012")).thenReturn(patient);

        mockMvc.perform(get("/api/patients/aadhaar/123456789012"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aadhaarNumber").value("123456789012"));
    }


}
