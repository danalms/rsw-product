--
-- !!! run this script second as the rsw_admin_user with rswdb set as default database !!!
-- This SQL can only be run via command line:
--  command line:  psql -U rsw_admin_user -d rswdb -a -f 0004-rsw-promoter21.sql
--
DROP SCHEMA IF EXISTS promoter21 CASCADE;
CREATE SCHEMA promoter21 AUTHORIZATION rsw_admin_role;
GRANT ALL ON SCHEMA promoter21 TO GROUP rsw_admin_role WITH GRANT OPTION;

GRANT USAGE ON SCHEMA promoter21 TO rsw_app_role;
GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER
ON ALL TABLES IN SCHEMA promoter21 TO GROUP rsw_app_role WITH GRANT OPTION;
GRANT USAGE
ON ALL SEQUENCES IN SCHEMA promoter21 TO GROUP rsw_app_role WITH GRANT OPTION;
GRANT EXECUTE
ON ALL FUNCTIONS IN SCHEMA promoter21 TO GROUP rsw_app_role WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA promoter21
GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON TABLES TO rsw_app_role;
ALTER DEFAULT PRIVILEGES IN SCHEMA promoter21
GRANT USAGE ON SEQUENCES TO rsw_app_role;
ALTER DEFAULT PRIVILEGES IN SCHEMA promoter21
GRANT EXECUTE ON FUNCTIONS TO rsw_app_role;

--
-- TEMPORARY: include promoter21 first as current schema for all future connections
--   Multitenancy will require dynamic routing to the correct schema datasource
--
ALTER DATABASE rswdb
SET search_path = promoter21,pg_catalog;


SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET edb_redwood_date = off;
SET default_with_rowids = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;
SET default_tablespace = '';
SET default_with_oids = false;

SET search_path = promoter21, pg_catalog, sys, dbo;

--
-- promoter
--
CREATE TABLE promoter (
    promoter_id bigint NOT NULL,
    promoter_status integer NOT NULL,
    promoter_name character varying(1000) NOT NULL,
    start_date timestamp without time zone,
    promoter_deleted boolean DEFAULT false NOT NULL,
    contact_info text,
    created_date timestamp with time zone NOT NULL DEFAULT current_timestamp,
    updated_date timestamp with time zone NOT NULL DEFAULT current_timestamp
);

ALTER TABLE promoter OWNER TO rsw_admin_role;

CREATE SEQUENCE promoter_promoter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE promoter_promoter_id_seq OWNER TO rsw_admin_role;

ALTER SEQUENCE promoter_promoter_id_seq OWNED BY promoter.promoter_id;

ALTER TABLE ONLY promoter ALTER COLUMN promoter_id SET DEFAULT nextval('promoter_promoter_id_seq'::regclass);

COPY promoter (promoter_id, promoter_status, promoter_name, start_date, promoter_deleted, contact_info) FROM stdin;
21	1	Category6 Racing	2014-06-01 00:00:00	f	Rick Patterson
\.

SELECT pg_catalog.setval('promoter_promoter_id_seq', 22, true);

ALTER TABLE ONLY promoter
    ADD CONSTRAINT pk_promoter PRIMARY KEY (promoter_id);

CREATE UNIQUE INDEX idx_promoter_1 ON promoter USING btree (promoter_id);


--
-- product
--
CREATE TABLE product (
    product_id bigint NOT NULL,
    product_name varchar(128) NOT NULL,
    description character varying(1000) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    sku varchar(20) NOT NULL,
    price NUMERIC(9,2) NOT NULL,
    created_date timestamp with time zone NOT NULL DEFAULT current_timestamp,
    updated_date timestamp with time zone NOT NULL DEFAULT current_timestamp
);

ALTER TABLE product OWNER TO rsw_admin_role;

CREATE SEQUENCE product_product_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER TABLE product_product_id_seq OWNER TO rsw_admin_role;

ALTER SEQUENCE product_product_id_seq OWNED BY product.product_id;

ALTER TABLE ONLY product ALTER COLUMN product_id SET DEFAULT nextval('product_product_id_seq'::regclass);

COPY product (product_id, product_name, description, start_date, end_date, sku, price) FROM stdin;
200	Test Product #200	This is the product description	2017-03-01 00:00:00	2020-12-07 00:00:00	4ZB123	22.30
202	Test Product #202	This is the product description	2017-03-01 00:00:00	2020-12-08 00:00:00	3DR100A	63.25
203	Test Product #203	This is the product description	2017-03-01 00:00:00	2020-12-10 00:00:00	3DR100B	47.25
204	Test Product #204	This is the product description	2017-03-10 00:00:00	2020-12-10 00:00:00	ARN-00A	0.25
205	Test Product #205	This is the product description	2017-03-10 00:00:00	2020-12-11 00:00:00	ARN-00B	0.27
206	Test Product #206	This is the product description	2017-03-11 00:00:00	2020-12-10 00:00:00	ARN-00C	0.75
207	Test Product #207	This is the product description	2017-03-12 00:00:00	2020-12-13 00:00:00	RHS001	1000.99
208	Test Product #208	This is the product description	2017-03-11 00:00:00	2020-12-21 00:00:00	PRS0030A	3120.00
209	Test Product #209	This is the product description	2017-03-09 00:00:00	2020-12-09 00:00:00	PRS0050D	3550.00
210	Test Product #210	This is the product description	2017-03-09 00:00:00	2020-12-09 00:00:00	PRS0070D	3995.00
\.

SELECT pg_catalog.setval('product_product_id_seq', 211, true);

ALTER TABLE ONLY product
    ADD CONSTRAINT pk_product PRIMARY KEY (product_id);

CREATE UNIQUE INDEX idx_product_1 ON product USING btree (product_id);

