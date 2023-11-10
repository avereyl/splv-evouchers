ALTER TABLE evoucher ADD COLUMN metadata_version INTEGER;
UPDATE evoucher SET metadata_version = 0;
ALTER TABLE evoucher ALTER COLUMN metadata_version SET NOT NULL;