--
-- !!! run this script second as the rsw_admin_user with rswdb set as default database !!!
-- This SQL can only be run via command line:
--  command line:  psql -U rsw_admin_user -d rswdb -a -f 0002-rsw-promoter1.sql
--
DROP SCHEMA IF EXISTS promoter1 CASCADE;
CREATE SCHEMA promoter1 AUTHORIZATION rsw_admin_role;
GRANT ALL ON SCHEMA promoter1 TO GROUP rsw_admin_role WITH GRANT OPTION;

GRANT USAGE ON SCHEMA promoter1 TO rsw_app_role;
GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER
ON ALL TABLES IN SCHEMA promoter1 TO GROUP rsw_app_role WITH GRANT OPTION;
GRANT USAGE
ON ALL SEQUENCES IN SCHEMA promoter1 TO GROUP rsw_app_role WITH GRANT OPTION;
GRANT EXECUTE
ON ALL FUNCTIONS IN SCHEMA promoter1 TO GROUP rsw_app_role WITH GRANT OPTION;

ALTER DEFAULT PRIVILEGES IN SCHEMA promoter1
GRANT INSERT, SELECT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER ON TABLES TO rsw_app_role;
ALTER DEFAULT PRIVILEGES IN SCHEMA promoter1
GRANT USAGE ON SEQUENCES TO rsw_app_role;
ALTER DEFAULT PRIVILEGES IN SCHEMA promoter1
GRANT EXECUTE ON FUNCTIONS TO rsw_app_role;

--
-- TEMPORARY: include promoter1 first as current schema for all future connections
--   Multitenancy will require dynamic routing to the correct schema datasource
--
ALTER DATABASE rswdb
SET search_path = promoter1,pg_catalog;


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

SET search_path = promoter1, pg_catalog, sys, dbo;

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
1	1	RSW Master Promoter	2017-03-01 00:00:00	f	Dan Alms
\.

SELECT pg_catalog.setval('promoter_promoter_id_seq', 2, true);

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
1	Test Product #1	This is the product description	2017-03-01 00:00:00	2020-06-07 14:55:36.06551-05	BB120	112.30
2	Test Product #2	This is the product description	2017-03-01 00:00:00	2020-06-08 16:14:44.363944-05	CR100A	57.25 
3	Test Product #3	This is the product description	2017-03-01 00:00:00	2020-06-10 08:46:34.286633-05	CR100B	67.25 
4	Test Product #4	This is the product description	2017-03-10 00:00:00	2020-06-10 08:47:41.606742-05	JRN-00A	0.25 
5	Test Product #5	This is the product description	2017-03-10 00:00:00	2020-06-11 00:10:12.29837-05	JRN-00B	7.25 
6	Test Product #6	This is the product description	2017-03-11 00:00:00	2020-06-10 15:50:53.417183-05	JRN-00C	7.75 
7	Test Product #7	This is the product description	2017-03-12 00:00:00	2020-06-13 21:00:10.678999-05	WHS001	1002.50 
8	Test Product #8	This is the product description	2017-03-11 00:00:00	2020-07-21 09:41:32.908159-05	FRS0030A	2120.00 
9	Test Product #9	This is the product description	2017-03-09 00:00:00	2020-06-09 13:42:59.523863-05	FRS0050D	2550.00 
10	Test Product #10	This is the product description	2017-03-09 00:00:00	2020-06-09 13:42:59.523863-05	FRS0070D	2995.00
\.

SELECT pg_catalog.setval('product_product_id_seq', 11, true);

ALTER TABLE ONLY product
    ADD CONSTRAINT pk_product PRIMARY KEY (product_id);

CREATE UNIQUE INDEX idx_product_1 ON product USING btree (product_id);

