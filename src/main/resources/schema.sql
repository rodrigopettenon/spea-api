CREATE TABLE tb_insumos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    quantidade_por_pacote DECIMAL(10,2) NOT NULL,
    valor_pago_por_pacote DECIMAL(10,2) NOT NULL
);

CREATE TABLE tb_receitas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    total_gasto_insumos DECIMAL(10,2) DEFAULT 0.00
);

CREATE TABLE tb_receita_insumo (
 insumo_id BIGINT NOT NULL,
 receita_id BIGINT NOT NULL,
 quantidade_utilizada_insumo DECIMAL(10,2) NOT NULL,
 valor_gasto_insumo DECIMAL(10,2) NOT NULL,
 PRIMARY KEY (insumo_Id, receita_id),
 CONSTRAINT fk_receita FOREIGN KEY (receita_id) REFERENCES tb_receitas(id) ON DELETE CASCADE,
 CONSTRAINT fk_insumo FOREIGN KEY (insumo_id) REFERENCES tb_insumos(id) ON DELETE CASCADE
 );