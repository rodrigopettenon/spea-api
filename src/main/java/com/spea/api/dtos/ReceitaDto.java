package com.spea.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceitaDto implements Serializable {

    private static final long serialVersionUID = -4622708278373228648L;

    private Long id;
    private String nome;
    private BigDecimal totalGastoInsumos;

    public ReceitaDto() {
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
