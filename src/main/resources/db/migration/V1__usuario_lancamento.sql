--CREATE DATABASE myfinances;

CREATE SCHEMA finances;

CREATE TABLE finances.usuario
(
  id bigserial NOT NULL PRIMARY KEY,
  nome character varying(150),
  email character varying(100),
  senha character varying(20),
  data_cadastro date default now()
);

CREATE TABLE finances.lancamento
(
  id bigserial NOT NULL PRIMARY KEY ,
  descricao character varying(100) NOT NULL,
  mes integer NOT NULL,
  ano integer NOT NULL,
  valor numeric(16,2),
  tipo character varying(20) check (tipo in ('RECEITA', 'DESPESA')) not null,
  status character varying(20) check (status in ('PENDENTE', 'CANCELADO', 'EFETIVADO')) not null,
  id_usuario bigint REFERENCES finances.usuario (id) not null,
  data_cadastro date default now()
);