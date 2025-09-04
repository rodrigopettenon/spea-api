package com.spea.api.controllers;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.services.ReceitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receita")
public class ReceitaController extends BaseController {

    @Autowired
    private ReceitaService receitaService;

    @Operation(
            summary = "Cadastra uma nova receita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "A receita foi cadastrada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarReceita(@RequestBody ReceitaDto receitaDto) {
        return createObjectReturn(receitaService.cadastrarReceita(receitaDto));
    }

    @Operation(
            summary = "Atualiza o nome da receita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "O nome da receita foi atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    @PutMapping("/atualizar-nome/{id}")
    public ResponseEntity<?> atualizarNomeDaReceita(@PathVariable(name = "id") Long id,
                                              @RequestBody ReceitaDto receitaDto) {
        return createObjectReturn(receitaService.atualizarNomeDaReceita(id, receitaDto));
    }

    @Operation(
            summary = "Obtém lista de receitas cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de receitas retornada com sucesso."),
    })
    @GetMapping("/lista")
    public ResponseEntity<?> obterListaDeReceitas() {
        return createObjectReturn(receitaService.obterListaDeReceitas());
    }
}
