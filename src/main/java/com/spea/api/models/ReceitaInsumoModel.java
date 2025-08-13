package com.spea.api.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_receita_insumo")
public class ReceitaInsumoModel implements Serializable{

    private static final long serialVersionUID = 7384914639432836104L;


    @EmbeddedId
    private ReceitaInsumoId id;

    @ManyToOne
    @MapsId("receitaId")
    @JoinColumn(name = "receita_id")
    private ReceitaModel receita;

    @ManyToOne
    @MapsId("insumoId")
    @JoinColumn(name = "insumo_id")
    private InsumoModel insumo;

    @Column(name = "quantidade_utilizada_insumo", nullable = false)
    private BigDecimal quantidadeUtilizadaInsumo;

    @Column(name = "valor_gasto_insumo", nullable = false)
    private BigDecimal valorGastoInsumo;

    public ReceitaInsumoModel() {
        super();
    }

    public ReceitaInsumoId getId() {
        return id;
    }

    public void setId(ReceitaInsumoId id) {
        this.id = id;
    }

    public ReceitaModel getReceita() {
        return receita;
    }

    public void setReceita(ReceitaModel receita) {
        this.receita = receita;
    }

    public InsumoModel getInsumo() {
        return insumo;
    }

    public void setInsumo(InsumoModel insumo) {
        this.insumo = insumo;
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
