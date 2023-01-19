CREATE TABLE print_config_pc (
id_print_config_pc NUMERIC,
dado BYTEA NOT NULL,
nome TEXT NOT NULL,
tipo TEXT NOT NULL,
PRIMARY KEY (id_print_config_pc)
);

CREATE TABLE trilha (
id_trilha NUMERIC,
descricao TEXT NOT NULL,
PRIMARY KEY (id_trilha)
);

CREATE TABLE linguagem (
id_linguagem NUMERIC,
nome TEXT NOT NULL,
PRIMARY KEY (id_linguagem)
);

CREATE TABLE edicao (
id_edicao NUMERIC,
nome TEXT NOT NULL,
PRIMARY KEY (id_edicao)
);

CREATE TABLE curriculo (
id_curriculo NUMERIC,
dado BYTEA NOT NULL,
nome TEXT NOT NULL,
tipo TEXT NOT NULL,
PRIMARY KEY (id_curriculo)
);

CREATE TABLE cargo (
id_cargo NUMERIC,
nome TEXT NOT NULL,
PRIMARY KEY (id_cargo)
);

CREATE TABLE gestor (
id_gestor NUMERIC,
nome TEXT NOT NULL,
email TEXT NOT NULL,
senha TEXT NOT NULL,
ativo TEXT NOT NULL,
genero TEXT NOT NULL,
cidade TEXT NOT NULL,
estado TEXT NOT NULL,
PRIMARY KEY (id_gestor)
);

CREATE TABLE gestor_cargo (
id_cargo NUMERIC NOT NULL,
id_gestor NUMERIC NOT NULL,
CONSTRAINT FK_GEST_CAR_ID_GEST
FOREIGN KEY (id_gestor)
REFERENCES gestor(id_gestor),
CONSTRAINT FK_GEST_CAR_ID_CArg
FOREIGN KEY (id_cargo)
REFERENCES cargo(id_cargo)
);

CREATE TABLE formulario (
id_formulario NUMERIC ,
id_curriculo NUMERIC,
id_print_config_pc NUMERIC NOT NULL,
instituicao TEXT NOT NULL,
lgpd TEXT NOT NULL,
github TEXT,
linkedin TEXT,
desafios TEXT NOT NULL,
problemas TEXT NOT NULL,
reconhecimento TEXT NOT NULL,
altruismo TEXT NOT NULL,
resposta TEXT ,
turno TEXT NOT NULL,
prova TEXT,
ingles TEXT NOT NULL,
espanhol TEXT NOT NULL,
neurodiversidade TEXT,
efetivacao TEXT NOT NULL,
disponibilidade TEXT NOT NULL,
genero TEXT NOT NULL,
orientacao TEXT NOT NULL,
matricula TEXT NOT NULL,
curso TEXT NOT NULL,
importancia_ti TEXT NOT NULL,
PRIMARY KEY (id_formulario)
);

CREATE TABLE trilha_form (
id_trilha NUMERIC NOT NULL,
id_formulario NUMERIC NOT NULL,
CONSTRAINT FK_TRI_FORM_ID_TRI
FOREIGN KEY (id_trilha)
REFERENCES trilha(id_trilha),
CONSTRAINT FK_FORM_TRI_ID_FORM
FOREIGN KEY (id_formulario)
REFERENCES formulario(id_formulario)
);

CREATE TABLE candidato (
id_candidato NUMERIC,
id_formulario NUMERIC NOT NULL,
id_edicao NUMERIC NOT NULL,
email TEXT NOT NULL,
nome TEXT NOT NULL,
data_nascimento DATE NOT NULL,
telefone TEXT NOT NULL,
rg TEXT NOT NULL,
cpf TEXT NOT NULL,
estado TEXT NOT NULL,
cidade TEXT NOT NULL,
pcd TEXT NOT NULL,
observacoes TEXT,
nota_comportamental NUMERIC(5,2),
nota_tecnica NUMERIC(5,2),
nota_prova NUMERIC(5,2),
ativo TEXT NOT NULL,
parecer_comp TEXT,
parecer_tecnico TEXT,
media NUMERIC(5,2),
PRIMARY KEY (id_candidato),
CONSTRAINT FK_CAND_ID_ED
FOREIGN KEY (id_edicao)
REFERENCES edicao(id_edicao)
);

CREATE TABLE linguagem_candidato (
id_linguagem NUMERIC ,
id_candidato NUMERIC NOT NULL,
CONSTRAINT FK_LING_CAND_ID_LING
FOREIGN KEY (id_linguagem)
REFERENCES linguagem(id_linguagem),
CONSTRAINT FK_LING_CAND_ID_CAND
FOREIGN KEY (id_candidato)
REFERENCES candidato(id_candidato)
);

CREATE TABLE inscricao (
id_inscricao NUMERIC,
id_candidato NUMERIC NOT NULL,
data_inscricao DATE NOT NULL,
avaliado TEXT NOT NULL,
PRIMARY KEY (id_inscricao)
);

CREATE TABLE avaliacao (
id_avaliacao NUMERIC ,
id_inscricao NUMERIC NOT NULL,
id_gestor NUMERIC NOT NULL,
aprovado TEXT NOT NULL,
PRIMARY KEY (id_avaliacao),
CONSTRAINT FK_AVA_ID_GEST
FOREIGN KEY (id_gestor)
REFERENCES gestor(id_gestor)
);

CREATE TABLE entrevistas (
id_entrevista NUMERIC,
id_gestor NUMERIC NOT NULL,
id_candidato NUMERIC NOT NULL,
legenda TEXT NOT NULL,
data_hora TIMESTAMP NOT NULL,
observacoes TEXT,
avaliadoENTREVISTA TEXT,
PRIMARY KEY (id_entrevista),
CONSTRAINT FK_ENT_ID_GEST
FOREIGN KEY (id_gestor)
REFERENCES gestor(id_gestor),
CONSTRAINT FK_ENT_ID_CAND
FOREIGN KEY (id_candidato)
REFERENCES candidato(id_candidato)
);

CREATE TABLE imagem (
id_imagem NUMERIC ,
id_gestor NUMERIC ,
id_candidato NUMERIC ,
dado BYTEA NOT NULL,
nome TEXT NOT NULL,
tipo TEXT NOT NULL,
PRIMARY KEY (id_imagem),
CONSTRAINT FK_GEST_IMG FOREIGN KEY (id_gestor) REFERENCES gestor (id_gestor),
CONSTRAINT FK_CAND_IMG FOREIGN KEY (id_candidato) REFERENCES candidato (id_candidato)
);

CREATE SEQUENCE seq_gestor
increment 1
start 1;

CREATE SEQUENCE seq_edicao
increment 1
start 1;

CREATE SEQUENCE seq_candidato
increment 1
start 1;

CREATE SEQUENCE seq_formulario
increment 1
start 1;

CREATE SEQUENCE seq_linguagem
increment 1
start 1;

CREATE SEQUENCE seq_cargo
increment 1
start 1;

CREATE SEQUENCE seq_print_config_pc
increment 1
start 1;

CREATE SEQUENCE seq_ENTREVISTA
increment 1
start 1;

CREATE SEQUENCE seq_avaliacao
increment 1
start 1;

CREATE SEQUENCE seq_imagem
increment 1
start 1;

CREATE SEQUENCE seq_curriculo
increment 1
start 1;

CREATE SEQUENCE seq_inscricao
increment 1
start 1;

CREATE SEQUENCE seq_trilha
increment 1
start 1;

INSERT INTO cargo (id_cargo, nome)
VALUES (nextval('seq_cargo'), 'ROLE_ADMINISTRADOR');

INSERT INTO cargo (id_cargo, nome)
VALUES (nextval('seq_cargo'), 'ROLE_INSTRUTOR');

INSERT INTO cargo (id_cargo, nome)
VALUES (nextval('seq_cargo'), 'ROLE_GESTAO');

INSERT INTO trilha (id_trilha, descricao)
VALUES (nextval('seq_trilha'), 'FRONTEND')

INSERT INTO trilha (id_trilha, descricao)
VALUES (nextval('seq_trilha'), 'BACKEND')

INSERT INTO trilha (id_trilha, descricao)
VALUES (nextval('seq_trilha'), 'QA')

INSERT INTO gestor (id_gestor, nome, email, senha, ativo, genero, cidade, estado)
VALUES (nextval('seq_gestor'), 'ADMIN', 'admin@dbccompany.com.br', '123', 'T', 'M', 'Porto Alegre', 'Rio Grande do Sul')