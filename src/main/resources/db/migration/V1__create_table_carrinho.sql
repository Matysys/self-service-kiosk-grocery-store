CREATE TABLE carrinho (
  id BIGINT AUTO_INCREMENT NOT NULL,
   id_produto BIGINT NOT NULL,
   nome_produto VARCHAR(20) NOT NULL,
   quantidade_produto INT NOT NULL,
   preco_produto DECIMAL NOT NULL,
   CONSTRAINT pk_carrinho PRIMARY KEY (id)
);