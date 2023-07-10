CREATE TABLE carrinho (
  id BIGINT AUTO_INCREMENT NOT NULL,
   nome_produto VARCHAR(20) NOT NULL,
   quantidade_produto INT NOT NULL,
   preco_produto DECIMAL(10, 2) NOT NULL,
   CONSTRAINT pk_carrinho PRIMARY KEY (id)
);