package com.spea.api.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_insumos")
public class InsumoModel implements Serializable {

    private static final long serialVersionUID = 1184072975136161933L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "quantidade_por_pacote", nullable = false)
    private Double quantidadePorPacote;

    @Column(name = "valor_pago_por_pacote", nullable = false)
    private BigDecimal valorPagoPorPacote;

    public InsumoModel() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getQuantidadePorPacote() {
        return quantidadePorPacote;
    }

    public void setQuantidadePorPacote(Double quantidadePorPacote) {
        this.quantidadePorPacote = quantidadePorPacote;
    }

    public BigDecimal getValorPagoPorPacote() {
        return valorPagoPorPacote;
    }

    public void setValorPagoPorPacote(BigDecimal valorPagoPorPacote) {
        this.valorPagoPorPacote = valorPagoPorPacote;
    }
}
