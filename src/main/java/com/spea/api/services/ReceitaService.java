package com.spea.api.services;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.spea.api.utils.LogUtil.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Transactional
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

    public ReceitaDto atualizarNomeDaReceita(Long id, ReceitaDto receitaDto) {
        logInicioAtualizacaoDaReceita(id);

        validarNomeDaReceita(receitaDto.getNome());
        verificarSeAReceitaExistePeloId(id);

        return receitaRepository.atualizarNomeDaReceita(id, receitaDto);
    }

    protected void verificarSeAReceitaExistePeloId(Long id) {
        logVerificacaoDeExistenciaDaReceita(id);

        if (!receitaRepository.verificarExistenciaDaReceitaPeloId(id)) {
            throw new EmpreendedorErrorException("O ID da receita informado não está cadastrado.");
        }

    }

    public List<ReceitaDto> obterListaDeReceitas() {
        logInicioObtencaoDeListaDeReceitas();

        return receitaRepository.obterListaDeReceitas();
    }
}
