package com.healthcare.patient_service.entity;
import jakarta.persistence.*;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;

// WHY "AppUser" and not "User"?
// "User" is a reserved keyword in PostgreSQL (and in some Spring Security classes).
// Using "User" would cause SQL errors: CREATE TABLE user ← fails in PostgreSQL!
// "AppUser" avoids this conflict.

@Entity
@Table(name = "users") // maps to the "users" table we created in V2
public class AppUser {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true , length = 50)
    private String username;

    // WHY length = 255 for password?
    // BCrypt hashes are always 60 characters.
    // But we use 255 to be safe if we switch to a different hashing algorithm later.

    @Column(nullable = false , length = 255)
    private String password;

    // WHY @Enumerated(EnumType.STRING)?
    // Without this, JPA stores the enum as a NUMBER (0, 1, 2).
    // If you reorder the enum values, the numbers shift and existing data breaks!
    // EnumType.STRING stores "ADMIN", "DOCTOR", "RECEPTIONIST" as readable text.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(name = "is_active",nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at",nullable = false, updatable = false)
    private LocalDateTime  createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }


    public Long getId() {return id;}
    public void setId(Long id){ this.id = id;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public Role getRole(){return role;};
    public void setRole(Role role){this.role = role;}

    public Boolean getIsActive(){return isActive;}
    public void setIsActive(Boolean isActive){ this.isActive = isActive;}

    public LocalDateTime getCreatedAt(){return createdAt;}

}
