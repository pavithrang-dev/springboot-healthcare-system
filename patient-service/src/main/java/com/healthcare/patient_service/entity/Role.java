package com.healthcare.patient_service.entity;

// WHY an enum instead of a String?
// If role was a String, someone could set role = "SUPERADMIN" or "god" — no validation.
// With an enum, Java enforces that role can ONLY be one of these 3 values.
// Compile-time safety > runtime checks.

public enum Role {
    ADMIN,          // Can do everything — manage users, view Aadhaar data
    DOCTOR,         // Can view patient list, view patient details
    RECEPTIONIST    // Can register new patients
}
