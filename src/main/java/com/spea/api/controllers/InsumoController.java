package com.spea.api.controllers;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.services.InsumoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insumo")
public class InsumoController extends BaseController{

    @Autowired
    private InsumoService insumoService;

    @Operation(summary = "Cadastra um novo insumo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insumo cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarInsumo(@RequestBody InsumoDto insumoDto) {
        return createObjectReturn(insumoService.cadastrarInsumo(insumoDto));
    }

    @Operation(summary = "Obtém lista de insumos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de insumos retornada com sucesso")
    })
    @GetMapping("/lista")
    public ResponseEntity<?> obterListaDeInsumos() {
        return createObjectReturn(insumoService.obterListaDeInsumos());
    }

    @Operation(
            summary = "Atualiza um insumo",
            description = "Atualiza os dados de um insumo existente e recalcula automaticamente os custos nas receitas relacionadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Insumo atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarInsumo(@PathVariable(name = "id") Long id,
                                             @RequestBody InsumoDto insumoDto) {
        return createObjectReturn(insumoService.atualizarInsumo(id, insumoDto));
    }

    @Operation(
            summary = "Deleta um insumo",
            description = "Deleta um insumo e ajusta os custos de todas as receitas onde ele era utilizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "O insumo foi deletado com sucesso."),
            @ApiResponse(responseCode = "400", description = "ID inválido ou ID não encontrado.")
    })
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarInsumo(@PathVariable(name = "id") Long id) {
        insumoService.deletarInsumo(id);
        return createObjectReturn("Insumo deletado com sucesso.");
    }
}