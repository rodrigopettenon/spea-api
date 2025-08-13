package com.spea.api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ReceitaInsumoId implements Serializable {

    @Column(name = "receita_id")
    private Long receitaId;

    @Column(name = "insumo_id")
    private Long insumoId;

    public ReceitaInsumoId() {
    }

    public ReceitaInsumoId(Long insumoId, Long receitaId) {
        this.insumoId = insumoId;
        this.receitaId = receitaId;
    }

    public Long getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(Long receitaId) {
        this.receitaId = receitaId;
    }

    public Long getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(Long insumoId) {
        this.insumoId = insumoId;
    }
}
