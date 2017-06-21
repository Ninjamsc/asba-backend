-- Hr_Persons

ALTER TABLE hr_persons
  ADD COLUMN doc_id BIGINT DEFAULT NULL;


-- Persons

ALTER TABLE persons
  DROP COLUMN personnel_number;