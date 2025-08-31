package com.spea.api.controllers;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.services.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receita")
public class ReceitaController extends BaseController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarReceita(@RequestBody ReceitaDto receitaDto) {
        return createObjectReturn(receitaService.cadastrarReceita(receitaDto));
    }

    @PutMapping("/atualizar-nome/{id}")
    public ResponseEntity<?> atualizarNomeDaReceita(@PathVariable(name = "id") Long id,
                                              @RequestBody ReceitaDto receitaDto) {
        return createObjectReturn(receitaService.atualizarNomeDaReceita(id, receitaDto));
    }

    @GetMapping("/lista")
    public ResponseEntity<?> obterListaDeReceitas() {
        return createObjectReturn(receitaService.obterListaDeReceitas());
    }
}
