package com.spea.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssociacaoDto implements Serializable {

    private static final long serialVersionUID = 7043023478796799285L;

    private InsumoDto insumoDto;
    private ReceitaDto receitaDto;
    private ReceitaInsumoDto receitaInsumoDto;

    public AssociacaoDto() {
    }

    public InsumoDto getInsumoDto() {
        return insumoDto;
    }

    public void setInsumoDto(InsumoDto insumoDto) {
        this.insumoDto = insumoDto;
    }

    public ReceitaDto getReceitaDto() {
        return receitaDto;
    }

    public void setReceitaDto(ReceitaDto receitaDto) {
        this.receitaDto = receitaDto;
    }

    public ReceitaInsumoDto getReceitaInsumoDto() {
        return receitaInsumoDto;
    }

    public void setReceitaInsumoDto(ReceitaInsumoDto receitaInsumoDto) {
        this.receitaInsumoDto = receitaInsumoDto;
    }
}
