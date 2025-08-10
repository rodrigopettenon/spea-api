package com.spea.api.controllers;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.services.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receita")
public class ReceitaController extends BaseController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarReceita(@RequestBody ReceitaDto receitaDto) {
        return createObjectReturn(receitaService.cadastrarReceita(receitaDto));
    }

}
