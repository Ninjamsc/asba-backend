CREATE TABLE request_traces (
  id                BIGSERIAL   NOT NULL CONSTRAINT request_traces_pkey PRIMARY KEY,
  request_wfm_id    BIGINT      NOT NULL,
  status            VARCHAR(50) NOT NULL,
  status_changed_at TIMESTAMP,
  comment           VARCHAR(2048)
);