package com.spea.api.dtos;

import java.io.Serializable;
import java.util.List;

public class GlobalPageDto<T> implements Serializable {

    private static final long serialVersionUID = -3222235915759544417L;

    private List<T> itens;
    private Long totalDeItens;
    private Integer totalDePaginas;
    private Integer paginaAtual;
    private Integer itensPorPagina;
    private Boolean temProxima;
    private Boolean temAnterior;

    public GlobalPageDto(List<T> itens, Long totalDeItens, Integer paginaAtual, Integer itensPorPagina) {
        this.itens = itens;
        this.totalDeItens = totalDeItens;
        this.paginaAtual = paginaAtual;
        this.itensPorPagina = itensPorPagina;

        if (itensPorPagina != null && itensPorPagina > 0) {
            this.totalDePaginas = (int) Math.ceil((double) totalDeItens / itensPorPagina);
        } else {
            this.totalDePaginas = 0;
        }
        this.temProxima = (paginaAtual + 1) < totalDePaginas;
        this.temAnterior = paginaAtual > 0;
    }

    public List<T> getItens() {
        return itens;
    }

    public Long getTotalDeItens() {
        return totalDeItens;
    }

    public Integer getTotalDePaginas() {
        return totalDePaginas;
    }

    public Integer getPaginaAtual() {
        return paginaAtual;
    }

    public Integer getItensPorPagina() {
        return itensPorPagina;
    }

    public Boolean getTemProxima() {
        return temProxima;
    }

    public Boolean getTemAnterior() {
        return temAnterior;
    }
}
