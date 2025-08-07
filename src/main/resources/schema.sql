CREATE TABLE tb_insumos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    quantidade_por_pacote DECIMAL(10,2) NOT NULL,
    valor_pago_por_pacote DECIMAL(10,2) NOT NULL
);