package com.spea.api.controllers;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.services.InsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insumo")
public class InsumoController extends BaseController{

    @Autowired
    private InsumoService insumoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarInsumo(@RequestBody InsumoDto insumoDto) {
        return createObjectReturn(insumoService.cadastrarInsumo(insumoDto));
    }

    @GetMapping("/lista")
    public ResponseEntity<?> obterListaDeInsumos() {
        return createObjectReturn(insumoService.obterListaDeInsumos());
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarInsumo(@PathVariable(name = "id") Long id,
                                             @RequestBody InsumoDto insumoDto) {
        return createObjectReturn(insumoService.atualizarInsumo(id, insumoDto));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarInsumo(@PathVariable(name = "id") Long id) {
        insumoService.deletarInsumo(id);
        return createObjectReturn("Insumo deletado com sucesso.");
    }
}