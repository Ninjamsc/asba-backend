ALTER TABLE front_ends DROP COLUMN fe_type RESTRICT;
ALTER TABLE front_ends DROP COLUMN version RESTRICT;
ALTER TABLE front_ends ADD COLUMN uuid varchar(255);
ALTER TABLE front_ends ADD COLUMN workstation_name varchar(255);
ALTER TABLE front_ends ADD COLUMN os_version varchar(255);
ALTER TABLE front_ends ADD COLUMN username varchar(255);
ALTER TABLE front_ends ADD COLUMN client_version varchar(255);