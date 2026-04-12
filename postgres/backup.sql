--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Debian 17.5-1.pgdg120+1)
-- Dumped by pg_dump version 17.5 (Debian 17.5-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: annotation_concepts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.annotation_concepts (
    annotation_id bigint NOT NULL,
    uri character varying(255) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.annotation_concepts OWNER TO postgres;

--
-- Name: annotations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.annotations (
    id bigint NOT NULL,
    annotation_type character varying(255) NOT NULL,
    date timestamp(6) without time zone NOT NULL,
    description character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    value text NOT NULL,
    page_url character varying(255) NOT NULL,
    campaign_id bigint,
    creator_id bigint,
    x integer NOT NULL,
    y integer NOT NULL
);


ALTER TABLE public.annotations OWNER TO postgres;

--
-- Name: annotations_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.annotations_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.annotations_seq OWNER TO postgres;

--
-- Name: campaigns; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.campaigns (
    id bigint NOT NULL,
    date timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    selected_thesauri character varying(255)[] NOT NULL,
    creator_id bigint
);


ALTER TABLE public.campaigns OWNER TO postgres;

--
-- Name: campaigns_participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.campaigns_participants (
    user_id bigint NOT NULL,
    campaign_id bigint NOT NULL
);


ALTER TABLE public.campaigns_participants OWNER TO postgres;

--
-- Name: campaigns_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.campaigns_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.campaigns_seq OWNER TO postgres;

--
-- Name: email_templates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email_templates (
    id bigint NOT NULL,
    content text NOT NULL,
    name character varying(255) NOT NULL,
    subject character varying(255) NOT NULL
);


ALTER TABLE public.email_templates OWNER TO postgres;

--
-- Name: email_templates_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.email_templates_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.email_templates_seq OWNER TO postgres;

--
-- Name: invitations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invitations (
    id bigint NOT NULL,
    campaign_id bigint,
    user_id bigint
);


ALTER TABLE public.invitations OWNER TO postgres;

--
-- Name: invitations_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.invitations_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.invitations_seq OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: roles_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_seq OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    activate boolean NOT NULL,
    email character varying(255) NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    role_id bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_seq OWNER TO postgres;

--
-- Name: verification_codes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.verification_codes (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    expiration_date timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.verification_codes OWNER TO postgres;

--
-- Name: verification_codes_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.verification_codes_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.verification_codes_seq OWNER TO postgres;

--
-- Data for Name: annotation_concepts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.annotation_concepts (annotation_id, uri, value) FROM stdin;
\.


--
-- Data for Name: annotations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.annotations (id, annotation_type, date, description, type, value, page_url, campaign_id, creator_id, x, y) FROM stdin;
\.


--
-- Data for Name: campaigns; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.campaigns (id, date, name, selected_thesauri, creator_id) FROM stdin;
\.


--
-- Data for Name: campaigns_participants; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.campaigns_participants (user_id, campaign_id) FROM stdin;
\.


--
-- Data for Name: email_templates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.email_templates (id, content, name, subject) FROM stdin;
1	<html lang="fr">\r\n  <head>\r\n    <meta charset="UTF-8" />\r\n    <meta name="viewport" content="width=device-width, initial-scale=1.0" />\r\n    <title>Password forgotten</title>\r\n  </head>\r\n  <body\r\n    style="\r\n      margin: 20px;\r\n      padding: 0;\r\n      font-family: Arial, sans-serif;\r\n      background-color: none;\r\n    "\r\n  >\r\n    <!-- Body -->\r\n    <div\r\n      style="\r\n        width: 100%;\r\n        max-width: 600px;\r\n        margin: auto;\r\n        background-color: #ffffff;\r\n        border-radius: 15px;\r\n        overflow: hidden;\r\n        border: 1px solid #d3d3d3;\r\n        text-align: center;\r\n      "\r\n    >\r\n      <!-- Header -->\r\n      <header style="background-color: #002d5e; padding: 1px">\r\n        <img\r\n          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"\r\n          alt="RDF logo"\r\n          style="\r\n            width: 100px;\r\n            max-width: 100%;\r\n            height: auto;\r\n            display: block;\r\n            margin: 5px auto;\r\n          "\r\n        />\r\n      </header>\r\n\r\n      <!-- Content -->\r\n      <div\r\n        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"\r\n      >\r\n        <h1 style="color: #3f4d75; margin-bottom: 20px">\r\n          PASSWORD FORGOTTEN ?\r\n        </h1>\r\n\r\n        <p>No worries !</p>\r\n\r\n        <img\r\n          src="https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3856/GIF_password.gif"\r\n          alt="Wrong Password Animation"\r\n          style="\r\n            max-width: 100%;\r\n            height: auto;\r\n            display: block;\r\n            margin: 20px auto;\r\n            border-radius: 10px;\r\n          "\r\n        />\r\n\r\n        <h1 style="color: #3f4d75">Here is your verification code</h1>\r\n\r\n        <span\r\n          style="\r\n            color: #3f4d75;\r\n            font-size: 30px;\r\n            font-weight: bold;\r\n            letter-spacing: 5px;\r\n          "\r\n          th:text="${code}"\r\n        >\r\n          123456\r\n        </span>\r\n\r\n      <!-- Divider -->\r\n      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />\r\n\r\n      <!-- Footer -->\r\n      <footer style="padding: 0 20px; font-size: 14px; color: #888">\r\n        <p>\r\n          If you have any questions, please do not hesitate to contact us at\r\n          <a\r\n            href="mailto:noreplyannotation@gmail.com"\r\n            style="color: #002d5e; text-decoration: none"\r\n            >noreplyannotation@gmail.com</a\r\n          >.\r\n        </p>\r\n        <p style="font-size: 12px; margin-top: 10px">\r\n          &copy; 2025 Yasmine Boudiaf. All rights reserved.\r\n        </p>\r\n      </footer>\r\n    </div>\r\n  </body>\r\n</html>	password_forgotten	Password forgotten
2	<html lang="fr">\r\n  <head>\r\n    <meta charset="UTF-8" />\r\n    <meta name="viewport" content="width=device-width, initial-scale=1.0" />\r\n    <title>Verification code expired</title>\r\n  </head>\r\n  <body\r\n    style="\r\n      margin: 20px;\r\n      padding: 0;\r\n      font-family: Arial, sans-serif;\r\n      background-color: none;\r\n    "\r\n  >\r\n    <!-- Body -->\r\n    <div\r\n      style="\r\n        width: 100%;\r\n        max-width: 600px;\r\n        margin: auto;\r\n        background-color: #ffffff;\r\n        border-radius: 15px;\r\n        overflow: hidden;\r\n        border: 1px solid #d3d3d3;\r\n        text-align: center;\r\n      "\r\n    >\r\n      <!-- Header -->\r\n      <header style="background-color: #002d5e; padding: 1px">\r\n        <img\r\n          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"\r\n          alt="RDF logo"\r\n          style="\r\n            width: 100px;\r\n            max-width: 100%;\r\n            height: auto;\r\n            display: block;\r\n            margin: 5px auto;\r\n          "\r\n        />\r\n      </header>\r\n\r\n      <!-- Content -->\r\n      <div\r\n        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"\r\n      >\r\n        <h1 style="color: #3f4d75; margin-bottom: 20px">\r\n          OOPS, YOUR VERIFICATION CODE HAS EXPIRED\r\n        </h1>\r\n\r\n        <p>Pas de soucis !</p>\r\n\r\n        <img\r\n          src="https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3856/GIF_password.gif"\r\n          alt="Wrong Password Animation"\r\n          style="\r\n            max-width: 100%;\r\n            height: auto;\r\n            display: block;\r\n            margin: 20px auto;\r\n            border-radius: 10px;\r\n          "\r\n        />\r\n\r\n        <h1 style="color: #3f4d75">Here is a new verification code</h1>\r\n\r\n        <span\r\n          style="\r\n            color: #3f4d75;\r\n            font-size: 30px;\r\n            font-weight: bold;\r\n            letter-spacing: 5px;\r\n          "\r\n          th:text="${code}"\r\n        >\r\n          123456\r\n        </span>\r\n\r\n        <p style="font-size: 12px; color: #888">\r\n          This code will expire in 1 hour.\r\n        </p>\r\n      </div>\r\n\r\n      <!-- Divider -->\r\n      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />\r\n\r\n      <!-- Footer -->\r\n      <footer style="padding: 0 20px; font-size: 14px; color: #888">\r\n        <p>\r\n          If you have any questions, please do not hesitate to contact us at\r\n          <a\r\n            href="mailto:noreplyannotation@gmail.com"\r\n            style="color: #002d5e; text-decoration: none"\r\n            >noreplyannotation@gmail.com</a\r\n          >.\r\n        </p>\r\n        <p style="font-size: 12px; margin-top: 10px">\r\n          &copy; 2025 Yasmine Boudiaf. All rights reserved.\r\n        </p>\r\n      </footer>\r\n    </div>\r\n  </body>\r\n</html>	verification_code_expired	Verification code expired
4	<html lang="fr">\r\n  <head>\r\n    <meta charset="UTF-8" />\r\n    <meta name="viewport" content="width=device-width, initial-scale=1.0" />\r\n    <title>New account</title>\r\n  </head>\r\n  <body\r\n    style="\r\n      margin: 20px;\r\n      padding: 0;\r\n      font-family: Arial, sans-serif;\r\n      background-color: none;\r\n    "\r\n  >\r\n    <!-- Body -->\r\n    <div\r\n      style="\r\n        width: 100%;\r\n        max-width: 600px;\r\n        margin: auto;\r\n        background-color: #ffffff;\r\n        border-radius: 15px;\r\n        overflow: hidden;\r\n        border: 1px solid #d3d3d3;\r\n        text-align: center;\r\n      "\r\n    >\r\n      <!-- Header -->\r\n      <header style="background-color: #002d5e; padding: 1px">\r\n        <img\r\n          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"\r\n          alt="RDF logo"\r\n          style="\r\n            width: 100px;\r\n            max-width: 100%;\r\n            height: auto;\r\n            display: block;\r\n            margin: 5px auto;\r\n          "\r\n        />\r\n      </header>\r\n\r\n      <!-- Content -->\r\n      <div\r\n        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"\r\n      >\r\n        <h1 style="color: #3f4d75; margin-bottom: 20px">\r\n          WELCOME TO OUR PLATFORM\r\n        </h1>\r\n\r\n        <p>\r\n          Welcome\r\n          <strong>\r\n            <span th:text="${firstName}">Jake</span>\r\n            <span th:text="${lastName}">Huswvfps</span>\r\n          </strong>\r\n        </p>\r\n\r\n        <p>\r\n          Thank you for joining us. Below is the link to verify your account.\r\n        </p>\r\n\r\n        <!-- Link -->\r\n        <div style="margin: 30px 0">\r\n          <a\r\n            th:href="${apiBaseUrl + &#39;/auth/validate?email=&#39; + email + &#39;&code=&#39; + code}"\r\n            style="\r\n              background-color: #002d5e;\r\n              color: #ffffff;\r\n              padding: 12px 30px;\r\n              border-radius: 5px;\r\n              text-decoration: none;\r\n              font-weight: bold;\r\n            "\r\n            >NEXT</a\r\n          >\r\n        </div>\r\n\r\n        <p style="font-size: 12px; color: #888">\r\n          This link will expire in 24 hours.\r\n        </p>\r\n      </div>\r\n\r\n      <!-- Divider -->\r\n      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />\r\n\r\n      <!-- Footer -->\r\n      <footer style="padding: 0 20px; font-size: 14px; color: #888">\r\n        <p>\r\n          If you have any questions, please do not hesitate to contact us at\r\n          <a\r\n            href="mailto:noreplyannotation@gmail.com"\r\n            style="color: #002d5e; text-decoration: none"\r\n            >noreplyannotation@gmail.com</a\r\n          >.\r\n        </p>\r\n        <p style="font-size: 12px; margin-top: 10px">\r\n          &copy; 2025 Yasmine Boudiaf. All rights reserved.\r\n        </p>\r\n      </footer>\r\n    </div>\r\n  </body>\r\n</html>	new_account	New account
3	<html lang="fr">\r\n  <head>\r\n    <meta charset="UTF-8" />\r\n    <meta name="viewport" content="width=device-width, initial-scale=1.0" />\r\n    <title>Invitation to take part in the campaign</title>\r\n  </head>\r\n  <body\r\n    style="\r\n      margin: 20px;\r\n      padding: 0;\r\n      font-family: Arial, sans-serif;\r\n      background-color: none;\r\n    "\r\n  >\r\n    <!-- Body -->\r\n    <div\r\n      style="\r\n        width: 100%;\r\n        max-width: 600px;\r\n        margin: auto;\r\n        background-color: #ffffff;\r\n        border-radius: 15px;\r\n        overflow: hidden;\r\n        border: 1px solid #d3d3d3;\r\n        text-align: center;\r\n      "\r\n    >\r\n      <!-- Header -->\r\n      <header style="background-color: #002d5e; padding: 1px">\r\n        <img\r\n          src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/Rdf_logo.svg/768px-Rdf_logo.svg.png"\r\n          alt="RDF logo"\r\n          style="\r\n            width: 100px;\r\n            max-width: 100%;\r\n            height: auto;\r\n            display: block;\r\n            margin: 5px auto;\r\n          "\r\n        />\r\n      </header>\r\n\r\n      <!-- Content -->\r\n      <div\r\n        style="padding: 20px; color: #555; font-size: 16px; line-height: 1.6"\r\n      >\r\n        <h1 style="color: #3f4d75; margin-bottom: 20px">\r\n          YOU HAVE AN INVITATION TO TAKE PART IN THE CAMPAIGN\r\n        </h1>\r\n\r\n        <p>\r\n          <strong>\r\n            <span th:text="${campaignName}">Campaign Name</span>\r\n          </strong>\r\n        </p>\r\n\r\n        <p>Below is the link to your invitation.</p>\r\n\r\n        <!-- Link -->\r\n        <div style="margin: 30px 0">\r\n          <a\r\n            th:href="${apiBaseUrl + &#39;/campaign/accept?participantEmail=&#39; + email + &#39;&campaignName=&#39; + campaignName}"\r\n            style="\r\n              background-color: #002d5e;\r\n              color: #ffffff;\r\n              padding: 12px 30px;\r\n              border-radius: 5px;\r\n              text-decoration: none;\r\n              font-weight: bold;\r\n            "\r\n            >NEXT</a\r\n          >\r\n        </div>\r\n      </div>\r\n\r\n      <!-- Divider -->\r\n      <hr style="border: none; border-top: 1px solid #d3d3d3; margin: 0 20px" />\r\n\r\n      <!-- Footer -->\r\n      <footer style="padding: 0 20px; font-size: 14px; color: #888">\r\n        <p>\r\n          If you have any questions, please do not hesitate to contact us at\r\n          <a\r\n            href="mailto:noreplyannotation@gmail.com"\r\n            style="color: #002d5e; text-decoration: none"\r\n            >noreplyannotation@gmail.com</a\r\n          >.\r\n        </p>\r\n        <p style="font-size: 12px; margin-top: 10px">\r\n          &copy; 2025 Yasmine Boudiaf. All rights reserved.\r\n        </p>\r\n      </footer>\r\n    </div>\r\n  </body>\r\n</html>	invitation_to_join_campaign	Join campaign
\.


--
-- Data for Name: invitations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.invitations (id, campaign_id, user_id) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
1	ADMIN
2	USER
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, activate, email, first_name, last_name, password, role_id) FROM stdin;
\.


--
-- Data for Name: verification_codes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verification_codes (id, code, email, expiration_date) FROM stdin;
\.


--
-- Name: annotations_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.annotations_seq', 201, true);


--
-- Name: campaigns_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.campaigns_seq', 251, true);


--
-- Name: email_templates_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.email_templates_seq', 1, false);


--
-- Name: invitations_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.invitations_seq', 1, false);


--
-- Name: roles_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_seq', 1, false);


--
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_seq', 201, true);


--
-- Name: verification_codes_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.verification_codes_seq', 201, true);


--
-- Name: annotations annotations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.annotations
    ADD CONSTRAINT annotations_pkey PRIMARY KEY (id);


--
-- Name: campaigns_participants campaigns_participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campaigns_participants
    ADD CONSTRAINT campaigns_participants_pkey PRIMARY KEY (user_id, campaign_id);


--
-- Name: campaigns campaigns_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campaigns
    ADD CONSTRAINT campaigns_pkey PRIMARY KEY (id);


--
-- Name: email_templates email_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_templates
    ADD CONSTRAINT email_templates_pkey PRIMARY KEY (id);


--
-- Name: invitations invitations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invitations
    ADD CONSTRAINT invitations_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: email_templates uk57lojflgusywyihcoxex20bml; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_templates
    ADD CONSTRAINT uk57lojflgusywyihcoxex20bml UNIQUE (name);


--
-- Name: campaigns uk5rhrd2ghk2m15qnrfc7xyv81i; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campaigns
    ADD CONSTRAINT uk5rhrd2ghk2m15qnrfc7xyv81i UNIQUE (name);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: verification_codes ukg6p4erxfa9j8bkiu2x3id5hrg; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_codes
    ADD CONSTRAINT ukg6p4erxfa9j8bkiu2x3id5hrg UNIQUE (email);


--
-- Name: roles ukofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: verification_codes verification_codes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verification_codes
    ADD CONSTRAINT verification_codes_pkey PRIMARY KEY (id);


--
-- Name: idx_annotation_campaign_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_annotation_campaign_id ON public.annotations USING btree (campaign_id);


--
-- Name: idx_annotation_creator_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_annotation_creator_id ON public.annotations USING btree (creator_id);


--
-- Name: idx_campaign_creator_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_campaign_creator_id ON public.campaigns USING btree (creator_id);


--
-- Name: idx_campaign_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_campaign_name ON public.campaigns USING btree (name);


--
-- Name: idx_invitation_campaign_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invitation_campaign_id ON public.invitations USING btree (campaign_id);


--
-- Name: idx_invitation_user_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_invitation_user_id ON public.invitations USING btree (user_id);


--
-- Name: idx_role_name; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_role_name ON public.roles USING btree (name);


--
-- Name: idx_user_activate; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_activate ON public.users USING btree (activate);


--
-- Name: idx_user_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_email ON public.users USING btree (email);


--
-- Name: idx_user_role_id; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_user_role_id ON public.users USING btree (role_id);


--
-- Name: idx_verification_email; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_verification_email ON public.verification_codes USING btree (email);


--
-- Name: idx_verification_expiration; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_verification_expiration ON public.verification_codes USING btree (expiration_date);


--
-- Name: campaigns_participants fk34q72ra4snrqj8upfimpquyem; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campaigns_participants
    ADD CONSTRAINT fk34q72ra4snrqj8upfimpquyem FOREIGN KEY (campaign_id) REFERENCES public.campaigns(id);


--
-- Name: invitations fk4wvuj0rfixe8dttrqk1o4qdh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invitations
    ADD CONSTRAINT fk4wvuj0rfixe8dttrqk1o4qdh FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: campaigns fkb9awn2dpeybv7lv6mhpketqte; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campaigns
    ADD CONSTRAINT fkb9awn2dpeybv7lv6mhpketqte FOREIGN KEY (creator_id) REFERENCES public.users(id);


--
-- Name: invitations fkctwe5vc57t62jo6kn1j9yxgfm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invitations
    ADD CONSTRAINT fkctwe5vc57t62jo6kn1j9yxgfm FOREIGN KEY (campaign_id) REFERENCES public.campaigns(id);


--
-- Name: annotations fkcvn5hlstis939grpygciko61v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.annotations
    ADD CONSTRAINT fkcvn5hlstis939grpygciko61v FOREIGN KEY (creator_id) REFERENCES public.users(id);


--
-- Name: annotations fkgxki9138bocp86eu5evm1wby3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.annotations
    ADD CONSTRAINT fkgxki9138bocp86eu5evm1wby3 FOREIGN KEY (campaign_id) REFERENCES public.campaigns(id);


--
-- Name: annotation_concepts fkjtoc25ldd056pyu1403i94rpg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.annotation_concepts
    ADD CONSTRAINT fkjtoc25ldd056pyu1403i94rpg FOREIGN KEY (annotation_id) REFERENCES public.annotations(id);


--
-- Name: campaigns_participants fknxdo9qx6wk92u8wb3jh6c83jc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.campaigns_participants
    ADD CONSTRAINT fknxdo9qx6wk92u8wb3jh6c83jc FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: users fkp56c1712k691lhsyewcssf40f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- PostgreSQL database dump complete
--

