-- WHY a separate users table?
-- Your Patient table stores PATIENT data (Aadhaar, address, etc.)
-- This table stores LOGIN credentials for doctors, receptionists, admins.
-- A patient is NOT a user. A user (doctor/receptionist) manages patients.

CREATE TABLE users (
    id          BIGSERIAL       PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,       -- stores BCrypt hash, NOT plaintext!
    role        VARCHAR(20)     NOT NULL,       -- ADMIN, DOCTOR, RECEPTIONIST
    is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- WHY index on username?
-- Every login does: SELECT * FROM users WHERE username = ?
-- Without index: full table scan (slow). With index: instant lookup.
CREATE INDEX idx_users_username ON users (username);

-- Insert a default admin user for testing
-- Password is BCrypt hash of "admin123"
-- You NEVER store plain text passwords in the database!
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');