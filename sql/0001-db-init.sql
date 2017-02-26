--
-- !!! run this script first as the default postgres user !!!
--  default installations create a 'postgres' user with a 'postgres' db and user is created with no password
-- This SQL can be run either via copy/paste into a client GUI or via command line
--  command line:  psql -U postgres -d postgres -a -f 0001-db-init.sql
--
-- This creates the rsw sample database and the following users:
--   1. rsw_admin_user (has superuser privs)
--   2. rsw_server_user (intended as TOMCAT user with limited r/w privs)
--   3. respective roles for each
--
DROP DATABASE IF EXISTS rswdb;
DROP SCHEMA IF EXISTS promoter1 CASCADE;
DROP SCHEMA IF EXISTS promoter20 CASCADE;
DROP SCHEMA IF EXISTS promoter21 CASCADE;
DROP ROLE IF EXISTS rsw_admin_role;
DROP ROLE IF EXISTS rsw_admin_user;
DROP ROLE IF EXISTS rsw_app_role;
DROP ROLE IF EXISTS rsw_server_user;

CREATE ROLE rsw_admin_role
SUPERUSER CREATEDB CREATEROLE REPLICATION
VALID UNTIL 'infinity';

CREATE ROLE rsw_admin_user LOGIN ENCRYPTED PASSWORD 'immv12#giver'
NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION INHERIT
VALID UNTIL 'infinity';

GRANT rsw_admin_role TO rsw_admin_user;

CREATE ROLE rsw_app_role
  NOSUPERUSER NOCREATEDB NOCREATEROLE REPLICATION
VALID UNTIL 'infinity';

CREATE ROLE rsw_server_user LOGIN ENCRYPTED PASSWORD 'fauns5_admix'
NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION INHERIT
VALID UNTIL 'infinity';

GRANT rsw_app_role TO rsw_server_user;

CREATE DATABASE rswdb
WITH ENCODING='UTF8'
OWNER=rsw_admin_role
CONNECTION LIMIT=-1;

GRANT ALL ON DATABASE rswdb TO GROUP rsw_admin_role WITH GRANT OPTION;
GRANT CONNECT ON DATABASE rswdb TO GROUP rsw_app_role WITH GRANT OPTION;

