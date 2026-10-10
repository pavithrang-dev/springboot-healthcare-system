package com.healthcare.patient_service.security;
import com.healthcare.patient_service.entity.AppUser;
import com.healthcare.patient_service.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// PURPOSE: This is the BRIDGE between Spring Security and YOUR database.
//
// WHEN Spring Security needs to verify a login, it calls:
//   userDetailsService.loadUserByUsername("dr_rajesh")
//
// Spring Security doesn't know about YOUR AppUser entity or YOUR database.
// It only understands its OWN interface: UserDetailsService.
// So this class TRANSLATES: AppUser (your entity) → UserDetails (Spring's format).
//
// ANALOGY: Spring Security speaks English. Your database speaks Tamil.
//          This class is the translator.
@Service
public class CustomUserDetailService implements UserDetailsService{
    private UserRepository userRepo;

    CustomUserDetailService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Step 1: Find user in YOUR database
        AppUser appUser =  userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found "+username));
        // Step 2: Convert YOUR AppUser to Spring Security's UserDetails
        // WHY "ROLE_" prefix?
        //   Spring Security expects authorities like "ROLE_ADMIN", "ROLE_DOCTOR".
        //   When you write hasRole("ADMIN") in config, Spring internally checks for "ROLE_ADMIN".
        //   This prefix convention is built into Spring Security — you must follow it.

        return User.builder().
                username(appUser.getUsername())
                .password(appUser.getPassword())  // the BCrypt hash from DB
                .roles(appUser.getRole().name())    // "ADMIN" → Spring adds "ROLE_" prefix
                .build();
    }
}

//        DaoAuthenticationProvider:
//        1. Calls customUserDetailsService.loadUserByUsername("dr_rajesh")
//  2. Gets back UserDetails with password hash + roles
//  3. Compares: BCrypt.matches(plainPassword, hashedPassword)
//  4. If match → authenticated! If not → 401
