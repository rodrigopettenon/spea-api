package com.spea.api.services;

import com.spea.api.dtos.GlobalPageDto;
import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.ReceitaInsumoRepository;
import com.spea.api.repositories.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.spea.api.utils.LogUtil.*;
import static com.spea.api.utils.StringUtil.normalizarEspacos;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.abbreviate;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@Transactional
public class ReceitaService {

    private static final Integer ITENS_POR_PAGINA = 10;
    private static final List<String> DIRECAO_PERMITIDA = Arrays.asList("asc", "desc");
    private static final Map<String, String> MAP_ORDENAR_POR_PERMITIDOS;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("nomeReceita", "nome");
        map.put("totalGastoInsumos", "total_gasto_insumos");
        MAP_ORDENAR_POR_PERMITIDOS = Collections.unmodifiableMap(map);
    }

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

    public GlobalPageDto<ReceitaDto> obterListaFiltradaEPaginadaDeReceitas(String nomeReceita, Integer paginaAtual, String direcao, String ordenarPor) {
        logInicioObtencaoDeListaFiltradaEPaginadaDeReceitas(nomeReceita);

        String nomeSemEspacosExtras = normalizarEspacos(nomeReceita);
        String direcaoCorrigida = corrigirDirecao(direcao);
        String ordenarPorCorrigido = corrigirOrdenarPor(ordenarPor);

        Long totalDeReceitas = receitaRepository
                .obterTotalDeReceitasFiltradasQuery(nomeSemEspacosExtras);


        Integer paginaAtualCorrigida = corrigirPaginaAtual(paginaAtual, totalDeReceitas, ITENS_POR_PAGINA);

        return receitaRepository.obterListaFiltradaEPaginadaDeReceitas(nomeSemEspacosExtras, paginaAtualCorrigida,
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

    public Integer corrigirPaginaAtual(Integer paginaAtual, Long totalDeReceitas, Integer itensPorPagina) {
        if (isNull(paginaAtual) || paginaAtual < 0) {
            return 0;
        }

        if (itensPorPagina == null || itensPorPagina <= 0) {
            return 0;
        }

        int totalDePaginas = (int) Math.ceil((double) totalDeReceitas / itensPorPagina);

        if (totalDePaginas == 0) {
            return 0;
        }

        if (paginaAtual >= totalDePaginas) {
            return totalDePaginas - 1;
        }

        return paginaAtual;
    }

}
