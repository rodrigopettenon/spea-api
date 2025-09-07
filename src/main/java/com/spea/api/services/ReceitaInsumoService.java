package com.spea.api.services;

import com.spea.api.dtos.*;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.InsumoRepository;
import com.spea.api.repositories.ReceitaInsumoRepository;
import com.spea.api.repositories.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.spea.api.utils.LogUtil.*;
import static com.spea.api.utils.StringUtil.normalizarEspacos;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Transactional
public class ReceitaInsumoService {

    private static final Integer ITENS_POR_PAGINA = 10;
    private static final List<String> DIRECAO_PERMITIDA = Arrays.asList("asc", "desc");
    private static final Map<String, String> MAP_ORDERNAR_POR_PERMITIDOS;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("nomeInsumo", "i.nome");
        map.put("quantidadeUtilizadaInsumo", "ri.quantidade_utilizada_insumo");
        map.put("valorGastoInsumo", "ri.valor_gasto_insumo");
        MAP_ORDERNAR_POR_PERMITIDOS = Collections.unmodifiableMap(map);
    }

    @Autowired
    private ReceitaInsumoRepository receitaInsumoRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Transactional
    public ReceitaInsumoDto criarAssociacao(Long receitaId, Long insumoId, BigDecimal quantidadeUtilizadaInsumo) {
        logInicioCriacaoDeAssociacao(receitaId, insumoId);

        verificarSeExisteAssociacaoDeInsumoEReceita(receitaId, insumoId);
        verificarQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
        verificarExistenciaDaReceitaPeloId(receitaId);
        verificarExistenciaDoInsumoPeloId(insumoId);

        logInicioDeObtencaoDaReceitaPorId(receitaId);
        ReceitaDto receitaEncontradaDto = receitaRepository.obterReceitaPeloId(receitaId);

        logInicioDeObtencaoDoInsumoPorId(insumoId);
        InsumoDto insumoEncontradoDto = insumoRepository.obterInsumoPeloId(insumoId);


        BigDecimal valorGastoInsumo = calcularGastoComInsumo(insumoEncontradoDto.getQuantidadePorPacote(),
                insumoEncontradoDto.getValorPagoPorPacote(), quantidadeUtilizadaInsumo);

        BigDecimal totalGastoInsumosAtualizado = receitaEncontradaDto.getTotalGastoInsumos().add(valorGastoInsumo);

        ReceitaDto receitaAtualizadaDto = new ReceitaDto();
        receitaAtualizadaDto.setNome(receitaEncontradaDto.getNome());
        receitaAtualizadaDto.setTotalGastoInsumos(totalGastoInsumosAtualizado);

        receitaRepository.atualizarTotalGastoInsumosDaReceita(receitaId, receitaAtualizadaDto);

        return receitaInsumoRepository.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, valorGastoInsumo);
    }

    private void verificarSeExisteAssociacaoDeInsumoEReceita(Long receitaId, Long insumoId) {
        logVerificacaoExistenciaDeAssociacaoEntreReceitaEInsumo(receitaId, insumoId);

        if (receitaInsumoRepository.verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId)) {
            throw new EmpreendedorErrorException("O insumo informado já está associado à receita informada.");
        }
    }

    private void verificarSeNaoExisteAssociacaoDeInsumoEReceita(Long receitaId, Long insumoId) {
        logVerificacaoExistenciaDeAssociacaoEntreReceitaEInsumo(receitaId, insumoId);

        if (!receitaInsumoRepository.verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId)) {
            throw new EmpreendedorErrorException("O insumo informado não está associado à receita informada.");
        }
    }


    private void verificarQuantidadeUtilizadaInsumo(BigDecimal quantidadeUtilizadaInsumo) {
        logVerificacaoDeQuantidadeUtilizadaDeInsumo(quantidadeUtilizadaInsumo);

        if (isNull(quantidadeUtilizadaInsumo)) {
            throw new EmpreendedorErrorException("A quantidade utilizada de insumo é obrigatória.");
        }
        if (quantidadeUtilizadaInsumo.signum() <= 0) {
            throw new EmpreendedorErrorException("A quantidade utilizada de insumo deve ser maior que 0.");
        }
    }

    protected BigDecimal calcularGastoComInsumo(Double quantidadePorPacote, BigDecimal valorPagoPorPacote,
                                              BigDecimal quantidadeUtilizadaInsumo) {
        validarQuantidadePorPacote(quantidadePorPacote);
        validarValorPagoPorPacote(valorPagoPorPacote);

        logInicioCalculoDeGastoComInsumo(quantidadePorPacote, valorPagoPorPacote, quantidadeUtilizadaInsumo);

        BigDecimal quantidadePorPacoteConvertida = BigDecimal.valueOf(quantidadePorPacote);

        BigDecimal precoPorUnidadeInsumo = valorPagoPorPacote.divide(quantidadePorPacoteConvertida, 6, RoundingMode.HALF_EVEN);

        return precoPorUnidadeInsumo.multiply(quantidadeUtilizadaInsumo)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private void validarValorPagoPorPacote(BigDecimal valorPagoPorPacote) {
        logValidacaoDoValorPagoPorPacoteDeInsumo(valorPagoPorPacote);

        if (isNull(valorPagoPorPacote)) {
            throw new EmpreendedorErrorException("O valor pago por pacote não pode ser nulo.");
        }
        if (valorPagoPorPacote.signum() <= 0) {
            throw new EmpreendedorErrorException("O valor pago por pacote deve ser maior que 0.");
        }
    }

    private void validarQuantidadePorPacote(Double quantidadePorPacote) {
        logValidacaoDaQuantidadeDeInsumoPorPacote(quantidadePorPacote);

        if (isNull(quantidadePorPacote)) {
            throw new EmpreendedorErrorException("A quantidade por pacote não pode ser nula.");
        }
        if (quantidadePorPacote <= 0) {
            throw new EmpreendedorErrorException("A quantidade por pacote deve ser maior que 0.");
        }
    }

    private void verificarExistenciaDoInsumoPeloId(Long insumoId) {
        logVerificacaoDeExistenciaDoInsumo(insumoId);

        if (!insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)) {
            throw new EmpreendedorErrorException("Nenhum insumo encontrado pelo id informado.");
        }
    }

    private void verificarExistenciaDaReceitaPeloId(Long receitaId) {
        logVerificacaoDeExistenciaDaReceita(receitaId);

        if (!receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)){
            throw new EmpreendedorErrorException("Nenhuma receita encontrada pelo id informado.");
        }
    }


    @Transactional
    public ReceitaInsumoDto atualizarQuantidadeUtilizadaInsumo(Long receitaId, Long insumoId, BigDecimal quantidadeUtilizadaInsumo) {
        logInicioAtualizacaoQuantidadeUtilizadaInsumo(receitaId, insumoId);
        verificarExistenciaDaReceitaPeloId(receitaId);
        verificarExistenciaDoInsumoPeloId(insumoId);
        verificarSeNaoExisteAssociacaoDeInsumoEReceita(receitaId, insumoId);
        verificarQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);

        AssociacaoDto associacaoEncontrada = receitaInsumoRepository
                .obterTodosOsDadosDaAssociacaoPorReceitaIdEInsumoId(receitaId, insumoId);

        ReceitaDto receitaEncontradaDto = associacaoEncontrada.getReceitaDto();

        InsumoDto insumoEncontradoDto = associacaoEncontrada.getInsumoDto();

        BigDecimal valorGastoInsumoAtualizado = calcularGastoComInsumo(insumoEncontradoDto.getQuantidadePorPacote(),
                insumoEncontradoDto.getValorPagoPorPacote(),
                quantidadeUtilizadaInsumo);

        ReceitaInsumoDto receitaInsumoEncontradoDto = associacaoEncontrada.getReceitaInsumoDto();

        BigDecimal valorGastoInsumoAntigo = receitaInsumoEncontradoDto.getValorGastoInsumo();
        BigDecimal totalGastoInsumosAtualizado = receitaEncontradaDto.getTotalGastoInsumos()
                .subtract(valorGastoInsumoAntigo)
                .max(BigDecimal.ZERO)
                .add(valorGastoInsumoAtualizado)
                .setScale(2, RoundingMode.HALF_EVEN);

        receitaEncontradaDto.setTotalGastoInsumos(totalGastoInsumosAtualizado);

        receitaRepository.atualizarTotalGastoInsumosDaReceita(receitaId, receitaEncontradaDto);

        return receitaInsumoRepository
                .atualizarReceitaInsumo(receitaId, insumoId, quantidadeUtilizadaInsumo, valorGastoInsumoAtualizado);
    }

    public GlobalPageDto<ReceitaInsumoDto> obterListaDeInsumosAssociadosAReceitaFiltradosEPaginados(Long receitaId, String nomeInsumo,
                                                                                  Integer paginaAtual, String direcao,
                                                                                  String ordenarPor) {
        logInicioObtencaoDaListaFiltradaEPaginadaDeInsumosAssociadosAReceita(receitaId);
        verificarExistenciaDaReceitaPeloId(receitaId);

        String nomeSemEspacosExtras = normalizarEspacos(nomeInsumo);
        String direcaoCorrigida = corrigirDirecao(direcao);
        String ordenarPorCorrigido = corrigirOrdenarPor(ordenarPor);

        Long totalDeInsumos = receitaInsumoRepository
                .obterTotalDeInsumosAssociadosAReceitaQuery(receitaId, nomeSemEspacosExtras);


        Integer paginaAtualCorrigida = corrigirPaginaAtual(paginaAtual, totalDeInsumos, ITENS_POR_PAGINA);


        return receitaInsumoRepository
                .obterListaFiltradaEPaginadaDeInsumosAssociadosAReceita(receitaId, nomeSemEspacosExtras, paginaAtualCorrigida,
                        ITENS_POR_PAGINA, direcaoCorrigida, ordenarPorCorrigido);
    }


    private Integer corrigirPaginaAtual(Integer paginaAtual, Long totalDeInsumos, Integer itensPorPagina) {
        if (isNull(paginaAtual) || paginaAtual < 0) {
            return 0;
        }

        if (itensPorPagina == null || itensPorPagina <= 0) {
            return 0;
        }

        int totalDePaginas = (int) Math.ceil((double) totalDeInsumos / itensPorPagina);

        if (totalDePaginas == 0) {
            return 0;
        }

        if (paginaAtual >= totalDePaginas) {
            return totalDePaginas - 1;
        }

        return paginaAtual;
    }


    private String corrigirDirecao(String direcao) {
        if (isBlank(direcao) || !DIRECAO_PERMITIDA.contains(direcao.toLowerCase())) {
            return "asc";
        }
        return direcao.toLowerCase();
    }

    private String corrigirOrdenarPor(String ordenarPor) {
        return MAP_ORDERNAR_POR_PERMITIDOS.getOrDefault(ordenarPor, "i.nome");
    }


}
