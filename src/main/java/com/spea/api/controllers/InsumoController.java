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

}
