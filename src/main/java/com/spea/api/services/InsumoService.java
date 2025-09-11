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
public class InsumoService {

    private static final Integer ITENS_POR_PAGINA = 10;
    private static final List<String> DIRECAO_PERMITIDA = Arrays.asList("asc", "desc");
    private static final Map<String, String> MAP_ORDENAR_POR_PERMITIDOS;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("nomeInsumo", "nome");
        map.put("quantidadePorPacote", "quantidade_por_pacote");
        map.put("valorPagoPorPacote", "valor_pago_por_pacote");
        MAP_ORDENAR_POR_PERMITIDOS = Collections.unmodifiableMap(map);
    }

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private ReceitaInsumoRepository receitaInsumoRepository;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private ReceitaInsumoService receitaInsumoService;

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

    @Transactional
    public InsumoDto atualizarInsumo(Long id, InsumoDto insumoDto) {
        logInicioDeAtualizacaoDoInsumo(id);

        validarNomeDoInsumo(insumoDto.getNome());
        validarQuantidadeDeInsumoPorPacote(insumoDto.getQuantidadePorPacote());
        validarValorPagoPorPacoteDeInsumo(insumoDto.getValorPagoPorPacote());
        verificarSeOInsumoExistePeloId(id);

        List<AssociacaoDto> listaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo = receitaInsumoRepository
                .obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id);

        for (AssociacaoDto associacao : listaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo) {
            ReceitaInsumoDto receitaInsumoDto = associacao.getReceitaInsumoDto();
            ReceitaDto receitaDto = associacao.getReceitaDto();

            BigDecimal gastoComInsumoAtualizado = receitaInsumoService
                    .calcularGastoComInsumo(insumoDto.getQuantidadePorPacote(),
                    insumoDto.getValorPagoPorPacote(),
                    receitaInsumoDto.getQuantidadeUtilizadaInsumo());

            receitaInsumoRepository.atualizarReceitaInsumo(receitaInsumoDto.getReceitaId(),
                    receitaInsumoDto.getInsumoId(),
                    receitaInsumoDto.getQuantidadeUtilizadaInsumo(),
                    gastoComInsumoAtualizado);

            BigDecimal valorGastoInsumoAntigo = receitaInsumoDto.getValorGastoInsumo();
            BigDecimal totalGastoInsumosAtualizado = receitaDto
                    .getTotalGastoInsumos()
                    .subtract(valorGastoInsumoAntigo)
                    .max(BigDecimal.ZERO)
                    .add(gastoComInsumoAtualizado)
                    .setScale(2, RoundingMode.HALF_EVEN);

            receitaDto.setTotalGastoInsumos(totalGastoInsumosAtualizado);

            receitaRepository.atualizarTotalGastoInsumosDaReceita(receitaDto.getId(), receitaDto);
        }

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

        List<AssociacaoDto> listaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo = receitaInsumoRepository
                .obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id);

        for (AssociacaoDto associacao : listaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo) {
            ReceitaInsumoDto receitaInsumoDto = associacao.getReceitaInsumoDto();
            ReceitaDto receitaDto = associacao.getReceitaDto();

            BigDecimal totalAtual = receitaDto.getTotalGastoInsumos();
            BigDecimal valorASubtrair = receitaInsumoDto.getValorGastoInsumo();

            verificarTotalAtualEValorASubtrair(totalAtual, valorASubtrair);

            BigDecimal valorGastoComInsumoExcluidoAbatido = totalAtual
                    .subtract(valorASubtrair)
                    .max(BigDecimal.ZERO)
                    .setScale(2, RoundingMode.HALF_EVEN);

            receitaDto.setTotalGastoInsumos(valorGastoComInsumoExcluidoAbatido);

            receitaRepository.atualizarTotalGastoInsumosDaReceita(receitaDto.getId(), receitaDto);
        }

        insumoRepository.deletarInsumo(id);
    }

    private void verificarTotalAtualEValorASubtrair(BigDecimal totalAtual, BigDecimal valorASubtrair) {
        logVerificacaoDeTotalAtualEValorASubtrair(totalAtual, valorASubtrair);

        if (totalAtual.compareTo(BigDecimal.ZERO) < 0) {
            throw new EmpreendedorErrorException("O total atual não pode ser negativo.");
        }

        if (valorASubtrair.compareTo(BigDecimal.ZERO) < 0) {
            throw new EmpreendedorErrorException("O valor gasto não pode ser negativo.");
        }
    }

    @Transactional(readOnly = true)
    public GlobalPageDto<InsumoDto> obterListaFiltradaEPaginadaDeInsumos(String nome, Integer paginaAtual,
                                                                         String direcao, String ordenarPor) {
        logInicioDeObtencaoDeInsumosFiltradosEPaginados();

        String nomeSemEspacosExtras = normalizarEspacos(nome);
        String direcaoCorrigida = corrigirDirecao(direcao);
        String ordenarPorCorrigido = corrigirOrdenarPor(ordenarPor);

        Long totalDeInsumos = insumoRepository
                .obterTotalDeInsumosFiltradosQuery(nomeSemEspacosExtras);


        Integer paginaAtualCorrigida = corrigirPaginaAtual(paginaAtual, totalDeInsumos, ITENS_POR_PAGINA);

        return insumoRepository.obterListaFiltradaEPaginadaDeInsumos(nomeSemEspacosExtras, paginaAtualCorrigida,
                ITENS_POR_PAGINA, direcaoCorrigida, ordenarPorCorrigido);
    }

    private String corrigirOrdenarPor(String ordenarPor) {
        return MAP_ORDENAR_POR_PERMITIDOS.getOrDefault(ordenarPor, "nome");
    }

    private String corrigirDirecao(String direcao) {
        if (isBlank(direcao) || !DIRECAO_PERMITIDA.contains(direcao.toLowerCase())) {
            return "asc";
        }
        return direcao.toLowerCase();
    }

    public Integer corrigirPaginaAtual(Integer paginaAtual, Long totalDeInsumos, Integer itensPorPagina) {
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
}
