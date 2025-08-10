package com.spea.api.services;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.spea.api.utils.LogUtil.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Transactional
    public ReceitaDto cadastrarReceita(ReceitaDto receitaDto) {
        logInicioCadastroDeReceita(receitaDto.getNome());

        validarNomeDaReceita(receitaDto.getNome());

        return receitaRepository.cadastrarReceita(receitaDto);
    }

    private void validarNomeDaReceita(String nome) {
        logValidacaoNomeDaReceita(nome);

        if (isBlank(nome)) {
            throw new EmpreendedorErrorException("O nome da receita é obrigatório");
        }
        if (nome.length() > 100) {
            throw new EmpreendedorErrorException("O nome da receita deve ter no máximo 100 caracteres");
        }
    }
}
