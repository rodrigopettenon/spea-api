package com.spea.api.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_receitas")
public class ReceitaModel implements Serializable {

    private static final long serialVersionUID = 2317332081777416490L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "total_gasto_insumos")
    private BigDecimal totalGastoInsumos;

    public ReceitaModel() {
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

    public BigDecimal getTotalGastoInsumos() {
        return totalGastoInsumos;
    }

    public void setTotalGastoInsumos(BigDecimal totalGastoInsumos) {
        this.totalGastoInsumos = totalGastoInsumos;
    }
}
