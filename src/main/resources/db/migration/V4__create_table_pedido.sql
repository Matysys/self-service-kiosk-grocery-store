CREATE TABLE pedidos (
  id BIGINT AUTO_INCREMENT NOT NULL,
   nome_produto VARCHAR(30) NOT NULL,
   id_produto BIGINT NOT NULL,
   preco_unitario DECIMAL(10, 2) NOT NULL,
   quantidade INT NOT NULL,
   forma_pagamento VARCHAR(255) NOT NULL,
   cod_venda char(36) NOT NULL,
   CONSTRAINT pk_pedidos PRIMARY KEY (id)
);