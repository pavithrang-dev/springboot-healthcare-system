CREATE TABLE patients (
    id                  BIGSERIAL       PRIMARY KEY,

    -- Personal Details
    full_name           VARCHAR(150)    NOT NULL,
    date_of_birth       DATE            NOT NULL,
    gender              VARCHAR(15)     NOT NULL,
    blood_group         VARCHAR(5),
    marital_status      VARCHAR(15),
    occupation          VARCHAR(100),
    preferred_language  VARCHAR(30)     DEFAULT 'English',

    -- Identity
    aadhaar_number      VARCHAR(12)     NOT NULL UNIQUE,

    -- Contact
    mobile_number       VARCHAR(13)     NOT NULL,
    email               VARCHAR(100),

    -- Address (India format)
    house_no            VARCHAR(50),
    street              VARCHAR(150),
    village_or_city     VARCHAR(100)    NOT NULL,
    district            VARCHAR(100)    NOT NULL,
    state               VARCHAR(50)     NOT NULL,
    pin_code            VARCHAR(6)      NOT NULL,

    -- Emergency Contact
    emergency_contact_name     VARCHAR(150),
    emergency_contact_relation VARCHAR(50),
    emergency_contact_mobile   VARCHAR(13),

    -- Audit
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by          VARCHAR(100),
    is_active           BOOLEAN         NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_patients_aadhaar ON patients (aadhaar_number);
CREATE INDEX idx_patients_mobile ON patients (mobile_number);
CREATE INDEX idx_patients_name ON patients (full_name);
CREATE INDEX idx_patients_pin_code ON patients (pin_code);