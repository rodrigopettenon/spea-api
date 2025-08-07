package com.spea.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsumoDto implements Serializable {

    private static final long serialVersionUID = 7407848195730281647L;

    private Long id;
    private String nome;
    private Double quantidadePorPacote;
    private BigDecimal valorPagoPorPacote;

    public InsumoDto() {
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
