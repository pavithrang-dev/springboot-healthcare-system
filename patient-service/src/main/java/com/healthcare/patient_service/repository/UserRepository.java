package com.healthcare.patient_service.repository;
import com.healthcare.patient_service.entity.AppUser;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
// WHY Optional<AppUser> and not AppUser?
// When someone logs in with a wrong username, there's no user to return.
// Optional forces you to HANDLE the "not found" case.
// Without Optional: NullPointerException at runtime (crash!)
// With Optional: you explicitly check isEmpty() or throw a meaningful error.

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long>{


    Optional<AppUser> findByUsername(String username);
    // Spring Data JPA automatically generates the SQL:
    // SELECT * FROM users WHERE username = ?
    // You write ZERO SQL. The method NAME is the query.
}
