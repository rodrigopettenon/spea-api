package com.spea.api.controllers;

import com.spea.api.services.ReceitaInsumoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/receita-insumo")
public class ReceitaInsumoController extends BaseController{

    @Autowired
    private ReceitaInsumoService receitaInsumoService;

    @Operation(
            summary = "Cria uma associação entre receita e insumo",
            description = "Adiciona um insumo a uma receita existente." +
                    " O sistema automaticamente calcula o custo total do insumo com base na quantidade e no valor fornecido," +
                    " e atualiza o custo total da receita com esse novo valor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A associação foi criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping("/receita/{receitaId}/insumo/{insumoId}")
    public ResponseEntity<?> criaAssociacao(@PathVariable(name = "receitaId") Long receitaId,
                                            @PathVariable(name = "insumoId") Long insumoId,
                                            @RequestParam BigDecimal quantidadeUtilizadaInsumo) {
        return createObjectReturn(receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));
    }

    @Operation(
            summary = "Atualiza quantidade utilizada de insumo",
            description = "Atualiza a quantidade de um insumo em uma receita existente e recalcula os custos totais.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PutMapping("/atualizar-quantidade/receita/{receitaId}/insumo/{insumoId}")
    public ResponseEntity<?> atualizarQuantidadeUtilizadaInsumo(@PathVariable(name = "receitaId") Long receitaId,
                                                                @PathVariable(name = "insumoId") Long insumoId,
                                                                @RequestParam BigDecimal quantidadeUtilizadaInsumo) {
        return createObjectReturn(receitaInsumoService
                .atualizarQuantidadeUtilizadaInsumo(receitaId, insumoId, quantidadeUtilizadaInsumo));
    }

}
