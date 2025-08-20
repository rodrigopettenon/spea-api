package com.spea.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceitaInsumoDto implements Serializable {

    private static final long serialVersionUID = -3069152328616554841L;

    private Long insumoId;
    private String insumoNome;
    private Long receitaId;
    private BigDecimal quantidadeUtilizadaInsumo;
    private BigDecimal valorGastoInsumo;

    public ReceitaInsumoDto() {
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

    public String getInsumoNome() {
        return insumoNome;
    }

    public void setInsumoNome(String insumoNome) {
        this.insumoNome = insumoNome;
    }

    public BigDecimal getQuantidadeUtilizadaInsumo() {
        return quantidadeUtilizadaInsumo;
    }

    public void setQuantidadeUtilizadaInsumo(BigDecimal quantidadeUtilizadaInsumo) {
        this.quantidadeUtilizadaInsumo = quantidadeUtilizadaInsumo;
    }

    public BigDecimal getValorGastoInsumo() {
        return valorGastoInsumo;
    }

    public void setValorGastoInsumo(BigDecimal valorGastoInsumo) {
        this.valorGastoInsumo = valorGastoInsumo;
    }
}
