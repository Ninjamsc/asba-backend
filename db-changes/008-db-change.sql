-- Table: public.skud_results

-- DROP TABLE public.skud_results;

CREATE TABLE public.skud_results
(
  id bigint NOT NULL,
  ins_date date NOT NULL,
  blur double precision,
  face_id bigint,
  face_square character varying(2048),
  height bigint,
  "timestamp" timestamp without time zone,
  person bigint,
  similarity double precision,
  url character varying(255),
  video_src character varying(255),
  width bigint,
  CONSTRAINT skud_results_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.skud_results
  OWNER TO biometry;