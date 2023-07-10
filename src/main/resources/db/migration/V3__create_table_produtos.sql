CREATE TABLE produtos (
  id BIGINT AUTO_INCREMENT NOT NULL,
   nome VARCHAR(30) NOT NULL,
   unidade_de_medida VARCHAR(10) NOT NULL,
   preco_unitario DECIMAL(10, 2) NOT NULL,
   categoria VARCHAR(30) NOT NULL,
   quantidade INT NOT NULL,
   CONSTRAINT pk_produtos PRIMARY KEY (id)
);