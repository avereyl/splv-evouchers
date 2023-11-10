ALTER TABLE evoucher DROP COLUMN created_by;
ALTER TABLE evoucher DROP COLUMN last_modified_by;
ALTER TABLE evoucher_event DROP COLUMN created_by;
ALTER TABLE evoucher_event DROP COLUMN last_modified_by;

DROP TABLE document_metadata;
DROP TABLE IF document_content;
DROP INDEX ix_document_metadata__evoucher;
