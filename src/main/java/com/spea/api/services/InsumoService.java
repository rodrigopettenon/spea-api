package com.spea.api.services;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.spea.api.utils.LogUtil.*;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Transactional
    public InsumoDto cadastrarInsumo(InsumoDto insumoDto) {
        logInicioCadastroDoInsumo(insumoDto.getNome());

        validarNomeDoInsumo(insumoDto.getNome());
        validarQuantidadeDeInsumoPorPacote(insumoDto.getQuantidadePorPacote());
        validarValorPagoPorPacoteDeInsumo(insumoDto.getValorPagoPorPacote());

        return insumoRepository.cadastrarInsumo(insumoDto);
    }

    private void validarNomeDoInsumo(String nome) {
        logValidacaoNomeDoInsumo(nome);

        if (isBlank(nome)) {
            throw new EmpreendedorErrorException("O nome do insumo é obrigatório.");
        }
        if (nome.length() > 100) {
            throw new EmpreendedorErrorException("O nome do insumo deve ter no máximo 100 caracteres.");
        }
    }

    private void validarQuantidadeDeInsumoPorPacote(Double quantidadePorPacote) {
        logValidacaoDaQuantidadeDeInsumoPorPacote(quantidadePorPacote);

        if (isNull(quantidadePorPacote)) {
            throw new EmpreendedorErrorException("A quantidade do insumo por pacote é obrigatória.");
        }
        if (quantidadePorPacote <= 0) {
            throw new EmpreendedorErrorException("A quantidade de insumo por pacote deve ser maior que 0.");
        }
    }

    private void validarValorPagoPorPacoteDeInsumo(BigDecimal valorPagoPorPacote) {
        logValidacaoDoValorPagoPorPacoteDeInsumo(valorPagoPorPacote);

        if (isNull(valorPagoPorPacote)) {
            throw new EmpreendedorErrorException("O valor pago por cada pacote de insumo é obrigatório.");
        }
        if (valorPagoPorPacote.signum() <= 0) {
            throw new EmpreendedorErrorException("O valor pago por cada pacote de insumo deve ser maior que 0.");
        }
    }

    @Transactional(readOnly = true)
    public List<InsumoDto> obterListaDeInsumos() {
        logInicioDeObtencaoDeInsumos();
        return insumoRepository.obterListaDeInsumos();
    }


    @Transactional
    public InsumoDto atualizarInsumo(Long id, InsumoDto insumoDto) {
        logInicioDeAtualizacaoDoInsumo(id);

        validarNomeDoInsumo(insumoDto.getNome());
        validarQuantidadeDeInsumoPorPacote(insumoDto.getQuantidadePorPacote());
        validarValorPagoPorPacoteDeInsumo(insumoDto.getValorPagoPorPacote());
        verificarSeOInsumoExistePeloId(id);

        return insumoRepository.atualizarInsumo(id, insumoDto);
    }

    private void verificarSeOInsumoExistePeloId(Long id) {
        logVerificacaoDeExistenciaDoInsumo(id);

        if (!insumoRepository.verificarExistenciaDoInsumoPeloId(id)) {
            throw new EmpreendedorErrorException("O ID do insumo informado não está cadastrado.");
        }
    }

    @Transactional
    public void deletarInsumo(Long id) {
        logInicioDeDelecaoDoInsumo(id);

        verificarSeOInsumoExistePeloId(id);
        insumoRepository.deletarInsumo(id);
    }

}
