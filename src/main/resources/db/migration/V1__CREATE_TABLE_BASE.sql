CREATE TABLE pessoa (
    id serial PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);


CREATE TABLE contacto (
    id serial PRIMARY KEY,
    tipo VARCHAR(50),
    valor VARCHAR(100),
    pessoa_id BIGINT,

    CONSTRAINT fk_contacto_pessoa
        FOREIGN KEY (pessoa_id)
        REFERENCES pessoa(id)
);


INSERT INTO pessoa (id, nome) VALUES
(1, 'Ana'),
(2, 'João'),
(3, 'Maria');

INSERT INTO contacto (id, tipo, valor, pessoa_id) VALUES
(10, 'EMAIL', 'ana@gmail.com', 1),
(11, 'TELEFONE', '9912345', 1),
(20, 'TELEFONE', '998877', 3);

