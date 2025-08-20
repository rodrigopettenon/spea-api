package com.spea.api.controllers;

import com.spea.api.services.ReceitaInsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/receita-insumo")
public class ReceitaInsumoController extends BaseController{

    @Autowired
    private ReceitaInsumoService receitaInsumoService;

    @PostMapping("/receita/{receitaId}/insumo/{insumoId}")
    public ResponseEntity<?> criaAssociacao(@PathVariable(name = "receitaId") Long receitaId,
                                            @PathVariable(name = "insumoId") Long insumoId,
                                            @RequestParam BigDecimal quantidadeUtilizadaInsumo) {
        return createObjectReturn(receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));
    }

}
