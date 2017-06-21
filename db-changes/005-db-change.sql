-- Persons

ALTER TABLE persons
  ADD COLUMN personnel_number VARCHAR(50) NOT NULL DEFAULT '';


-- HR_Persons

CREATE TABLE hr_persons (
  id               BIGSERIAL   NOT NULL,
  personnel_number VARCHAR(50) NOT NULL,
  first_name       VARCHAR(50) NOT NULL,
  middle_name      VARCHAR(50) NOT NULL,
  last_name        VARCHAR(50) NOT NULL,
  created_at       TIMESTAMP   NOT NULL,
  updated_at       TIMESTAMP   NOT NULL
);

ALTER TABLE hr_persons
    ADD CONSTRAINT hr_persons_pk PRIMARY KEY (id);

CREATE UNIQUE INDEX hr_persons_uk ON hr_persons(personnel_number);