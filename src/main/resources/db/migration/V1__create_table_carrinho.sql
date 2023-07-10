CREATE TABLE carrinho (
  id BIGINT AUTO_INCREMENT NOT NULL,
   nome_produto VARCHAR(20) NOT NULL,
   quantidade_produto VARCHAR(255) NOT NULL,
   preco_produto INT NOT NULL,
   CONSTRAINT pk_carrinho PRIMARY KEY (id)
);