DROP SCHEMA IF EXISTS oauth CASCADE;

-- DROP SCHEMA oauth;


CREATE SCHEMA oauth AUTHORIZATION postgres;

SET schema 	'oauth';
-- Permissions

GRANT ALL ON SCHEMA oauth TO postgres;

-- tb_permissions definition

-- Drop table

-- DROP TABLE tb_permissions;

CREATE TABLE tb_permissions (
                                id int8 NOT NULL,
                                perm_name varchar(255) NULL,
                                CONSTRAINT tb_permissions_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE tb_permissions OWNER TO postgres;
GRANT ALL ON TABLE tb_permissions TO postgres;


-- tb_roles definition

-- Drop table

-- DROP TABLE tb_roles;

CREATE TABLE tb_roles (
                          id int8 NOT NULL,
                          role_name varchar(255) NULL,
                          CONSTRAINT tb_roles_pkey PRIMARY KEY (id)
);

-- Permissions

ALTER TABLE tb_roles OWNER TO postgres;
GRANT ALL ON TABLE tb_roles TO postgres;


-- tb_permission_roles definition

-- Drop table

-- DROP TABLE tb_permission_roles;

CREATE TABLE tb_permission_roles (
                                     id int8 NOT NULL,
                                     permission_id int8 NULL,
                                     role_id int8 NULL,
                                     CONSTRAINT tb_permission_roles_pkey PRIMARY KEY (id),
                                     CONSTRAINT fk4mbyinmp5sga0fixknq4wcai FOREIGN KEY (role_id) REFERENCES tb_roles(id),
                                     CONSTRAINT fk62q0l8g4uew289kw0y9nal8ko FOREIGN KEY (permission_id) REFERENCES tb_permissions(id)
);

-- Permissions

ALTER TABLE tb_permission_roles OWNER TO postgres;
GRANT ALL ON TABLE tb_permission_roles TO postgres;


-- tb_user_role definition

-- Drop table

-- DROP TABLE tb_user_role;

CREATE TABLE tb_user_role (
                              dt_include_regt timestamp(6) NULL,
                              role_code int8 NULL,
                              credentials varchar(255) NOT NULL,
                              CONSTRAINT tb_user_role_pkey PRIMARY KEY (credentials),
                              CONSTRAINT fk1xwjj9nj15w3pmt4cyn7tinux FOREIGN KEY (role_code) REFERENCES tb_roles(id)
);

-- Permissions

ALTER TABLE tb_user_role OWNER TO postgres;
GRANT ALL ON TABLE tb_user_role TO postgres;


-- tb_session definition

-- Drop table

-- DROP TABLE tb_session;

CREATE TABLE tb_session (
                            dt_expiration_regt timestamp(6) NULL,
                            dt_include_regt timestamp(6) NULL,
                            id int8 NOT NULL,
                            credentials varchar(255) NULL,
                            CONSTRAINT tb_session_pkey PRIMARY KEY (id),
                            CONSTRAINT fkm8eadlrxgv6ox75q3eo32yodn FOREIGN KEY (credentials) REFERENCES tb_user_role(credentials)
);

-- Permissions

ALTER TABLE tb_session OWNER TO postgres;
GRANT ALL ON TABLE tb_session TO postgres;


-- tb_permissions_roles_seq definition

-- DROP SEQUENCE tb_permissions_roles_seq;

CREATE SEQUENCE tb_permissions_roles_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE tb_permissions_roles_seq OWNER TO postgres;
GRANT ALL ON SEQUENCE tb_permissions_roles_seq TO postgres;


-- tb_permissions_seq definition

-- DROP SEQUENCE tb_permissions_seq;

CREATE SEQUENCE tb_permissions_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE tb_permissions_seq OWNER TO postgres;
GRANT ALL ON SEQUENCE tb_permissions_seq TO postgres;


-- tb_user_session_seq definition

-- DROP SEQUENCE tb_user_session_seq;

CREATE SEQUENCE tb_user_session_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

-- Permissions

ALTER SEQUENCE tb_user_session_seq OWNER TO postgres;
GRANT ALL ON SEQUENCE tb_user_session_seq TO postgres;


INSERT INTO tb_permissions (id, perm_name) VALUES( 1, 'REGISTER_USER');
INSERT INTO tb_permissions (id, perm_name) VALUES( 2, 'ERASE_USER');
INSERT INTO tb_permissions (id, perm_name) VALUES( 3, 'SEND_EMAIL');
INSERT INTO tb_permissions (id, perm_name) VALUES( 4, 'RECEIVE_EMAIL');
INSERT INTO tb_permissions (id, perm_name) VALUES( 5, 'LOGIN');
INSERT INTO tb_permissions (id, perm_name) VALUES( 6, 'PANEL');
INSERT INTO tb_permissions (id, perm_name) VALUES( 7, 'PROFILE_EDIT');
INSERT INTO tb_permissions (id, perm_name) VALUES( 8, 'FULL');

INSERT INTO tb_roles (id, role_name) VALUES(1,'BASIC');
INSERT INTO tb_roles (id, role_name) VALUES(2,'INTERMEDIARY');
INSERT INTO tb_roles (id, role_name) VALUES(3,'FULL');
INSERT INTO tb_roles (id, role_name) VALUES(4,'BLOCKED');

INSERT INTO tb_permission_roles (id, permission_id, role_id) VALUES (nextval('tb_permissions_roles_seq'), 6, 1);
INSERT INTO tb_permission_roles (id, permission_id, role_id) VALUES (nextval('tb_permissions_roles_seq'), 1, 1);
INSERT INTO tb_permission_roles (id, permission_id, role_id) VALUES (nextval('tb_permissions_roles_seq'), 2, 1);
INSERT INTO tb_permission_roles (id, permission_id, role_id) VALUES (nextval('tb_permissions_roles_seq'), 5, 1);

INSERT INTO tb_user_role (dt_include_regt, role_code, credentials) VALUES(current_date, 1, 'asnisdy-s1ds8as4d-asdna');

