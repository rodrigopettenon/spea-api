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

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarReceita(@PathVariable(name = "id") Long id,
                                              @RequestBody ReceitaDto receitaDto) {
        return createObjectReturn(receitaService.atualizarReceita(id, receitaDto));
    }

}
