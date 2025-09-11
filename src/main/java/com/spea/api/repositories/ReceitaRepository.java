package com.spea.api.repositories;

import com.spea.api.dtos.GlobalPageDto;
import com.spea.api.dtos.InsumoDto;
import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spea.api.utils.LogUtil.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Repository
public class ReceitaRepository {

    @PersistenceContext
    private EntityManager em;


    public ReceitaDto cadastrarReceita(ReceitaDto receitaDto) {
        try {
            String sql = " INSERT INTO tb_receitas (nome) VALUES (:nome) ";

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("nome", receitaDto.getNome());

            query.executeUpdate();

            logSucessoAoCadastrarReceita(receitaDto.getNome());
            return receitaDto;
        }catch (Exception e) {
            logErroInesperadoAoCadastrarReceita(receitaDto.getNome(), e);
            throw new EmpreendedorErrorException("Erro inesperado ao cadastrar receita.");
        }
    }

    public ReceitaDto obterReceitaPeloId(Long id) {
        try{
            String sql = " SELECT id, nome, total_gasto_insumos FROM tb_receitas WHERE id = :id LIMIT 1 ";

            Query query = em.createNativeQuery(sql)
                    .setParameter("id", id);

            List<Object[]> resultList = query.getResultList();

            if (resultList.isEmpty()) {
                throw new EmpreendedorErrorException("Nenhuma receita encontrada pelo id informado.");
            }

            Object[] result = resultList.get(0);

            ReceitaDto receitaEncontradaDto = new ReceitaDto();
            receitaEncontradaDto.setId(((Number) result[0]).longValue());
            receitaEncontradaDto.setNome((String) result[1]);

            BigDecimal totalGastoInsumos = new BigDecimal(result[2].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            receitaEncontradaDto.setTotalGastoInsumos(totalGastoInsumos);

            logSucessoAoObterReceitaPeloId(id);
            return receitaEncontradaDto;

        } catch (EmpreendedorErrorException e) {
            throw e;
        }
        catch (Exception e) {
            logErroInesperadoAoObterReceitaPeloId(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao buscar receita pelo id.");
        }
    }

    public ReceitaDto atualizarTotalGastoInsumosDaReceita(Long id, ReceitaDto receitaDto) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE tb_receitas ");
            sql.append(" SET total_gasto_insumos = :total_gasto_insumos ");
            sql.append(" WHERE id = :id LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("total_gasto_insumos", receitaDto.getTotalGastoInsumos())
                    .setParameter("id", id);

            query.executeUpdate();
            logSucessoAoAtualizarReceita(id);

            ReceitaDto receitaDtoAtualizada = new ReceitaDto();
            receitaDtoAtualizada.setId(id);
            receitaDtoAtualizada.setNome(receitaDto.getNome());
            receitaDtoAtualizada.setTotalGastoInsumos(receitaDto.getTotalGastoInsumos());

            return receitaDto;

        }catch (Exception e) {
            logErroInesperadoAoAtualizarReceita(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao atualizar receita.");
        }
    }

    public Boolean verificarExistenciaDaReceitaPeloId(Long id) {
        try {
            String sql = " SELECT 1 FROM tb_receitas WHERE id = :id LIMIT 1 ";

             Query query = em.createNativeQuery(sql)
                     .setParameter("id", id);

            List<?> listaDeResultado = query.getResultList();
            logSucessoAoVerificarExistenciaDaReceita(id);

            return !listaDeResultado.isEmpty();

        }catch (Exception e) {
            logErroInesperadoAoVerificarExistenciaDaReceita(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao verificar existência da receita pelo id.");
        }
    }

    public ReceitaDto atualizarNomeDaReceita(Long id, ReceitaDto receitaDto) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE tb_receitas ");
            sql.append(" SET nome = :nome ");
            sql.append(" WHERE id = :id LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("nome", receitaDto.getNome())
                    .setParameter("id", id);

            query.executeUpdate();
            logSucessoAoAtualizarReceita(id);

            ReceitaDto receitaDtoAtualizada = new ReceitaDto();
            receitaDtoAtualizada.setId(id);
            receitaDtoAtualizada.setNome(receitaDto.getNome());

            logSucessoAoAtualizarReceita(id);
            return receitaDto;
        } catch (Exception e) {
            logErroInesperadoAoAtualizarReceita(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao atualizar o nome da receita.");
        }
    }

    public GlobalPageDto<ReceitaDto> obterListaFiltradaEPaginadaDeReceitas(String nome, Integer paginaAtual,
                                                                          Integer itensPorPagina, String direcao,
                                                                          String ordenarPor) {
        Long totalDeReceitas = obterTotalDeReceitasFiltradasQuery(nome);

        List<ReceitaDto> listaFiltradaEPaginadaDeReceitas =
                obterListaOrdenadaFiltradaEPaginadaDeReceitasQuery(nome, paginaAtual, itensPorPagina, direcao, ordenarPor);

        GlobalPageDto<ReceitaDto> listaDeReceitasFiltradasEPaginadas =
                new GlobalPageDto<>(listaFiltradaEPaginadaDeReceitas, totalDeReceitas, paginaAtual, itensPorPagina);

        return listaDeReceitasFiltradasEPaginadas;

    }

    public Long obterTotalDeReceitasFiltradasQuery(String nome) {
        try{
            Map<String, Object> parametros = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT COUNT(*) FROM tb_receitas ");

            if (isNotBlank(nome)) {
                sql.append(" WHERE LOWER(nome) LIKE LOWER(:nome) ");
                parametros.put("nome", "%" + nome + "%");
            }

            Query query = em.createNativeQuery(sql.toString());

            setQueryParameters(parametros, query);

            Object resultado = query.getSingleResult();
            Number totalDeReceitas = (Number) resultado;

            logSucessoAoObterTotalDeReceitasFiltradas(nome);
            return totalDeReceitas.longValue();

        } catch (Exception e) {
            logErroInesperadoAoObterTotalDeReceitasFiltradas(nome, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter total de receitas filtradas.");
        }
    }

    private List<ReceitaDto> obterListaOrdenadaFiltradaEPaginadaDeReceitasQuery(String nome, Integer paginaAtual,
                                                                                Integer itensPorPagina, String direcao,
                                                                                String ordenarPor) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT id, nome, total_gasto_insumos FROM tb_receitas ");

            if (isNotBlank(nome)) {
                sql.append(" WHERE LOWER(nome) LIKE LOWER(:nome) ");
                parametros.put("nome", "%" + nome + "%");
            }

            sql.append(" ORDER BY ").append(ordenarPor).append(" ").append(direcao).append(" ");
            sql.append(" LIMIT :limit OFFSET :offset ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("limit", itensPorPagina)
                    .setParameter("offset", paginaAtual * itensPorPagina);

            setQueryParameters(parametros, query);

            List<Object[]> listaDeResultados = query.getResultList();
            List<ReceitaDto> listaFiltradaEPaginadaDeReceitas = new ArrayList<>();

            for (Object[] resultado : listaDeResultados) {
                ReceitaDto receitaDto = new ReceitaDto();

                receitaDto.setId(((Number) resultado[0]).longValue());
                receitaDto.setNome((String) resultado[1]);

                BigDecimal totalGastoInsumos = new BigDecimal(resultado[2].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaDto.setTotalGastoInsumos(totalGastoInsumos);

                listaFiltradaEPaginadaDeReceitas.add(receitaDto);
            }

            logSucessoAoObterListaFiltradaEPaginadaDeReceitas(nome);
            return listaFiltradaEPaginadaDeReceitas;

        } catch (Exception e) {
            logErroInesperadoAoObterListaFiltradaEPaginadaDeReceitas(nome, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter lista de receitas filtradas e paginadas.");
        }
    }

    private void setQueryParameters(Map<String, Object> parameters, Query query) {
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
    }
}
