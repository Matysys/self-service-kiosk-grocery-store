CREATE TABLE produtos (
  id BIGINT AUTO_INCREMENT NOT NULL,
   nome VARCHAR(30) NOT NULL,
   unidade_de_medida VARCHAR(10) NOT NULL,
   preco_unitario DECIMAL NOT NULL,
   categoria VARCHAR(30) NOT NULL,
   CONSTRAINT pk_produtos PRIMARY KEY (id)
);