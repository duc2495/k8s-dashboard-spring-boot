# psql -h localhost -p 5432 -U postgres
DROP DATABASE IF EXISTS "k8s-client";
CREATE DATABASE "k8s-client";
\connect "k8s-client"

CREATE TABLE customer
(
  customer_id SERIAL,
  full_name character varying(50) NOT NULL,
  email character varying(50) NOT NULL,
  password character varying(200) NOT NULL,
  PRIMARY KEY (customer_id),
  unique (email)
)WITH (
  OIDS=FALSE
);

CREATE TABLE project
(
  project_id SERIAL,
  customer_id integer NOT NULL,
  project_name character varying(50) NOT NULL,
  description character varying,
  PRIMARY KEY (project_id),
  unique (project_name),
  CONSTRAINT customer_id_fkey FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
)WITH (
  OIDS=FALSE
);

CREATE TABLE application
(
  app_id SERIAL,
  project_id integer NOT NULL,
  app_name character varying(50) NOT NULL,
  description character varying,
  pro_autoscaler boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (app_id),
  CONSTRAINT application_id_fkey FOREIGN KEY (project_id) REFERENCES project (project_id)
)WITH (
  OIDS=FALSE
);
