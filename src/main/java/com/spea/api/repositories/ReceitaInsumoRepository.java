package com.spea.api.repositories;

import com.spea.api.dtos.*;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spea.api.utils.LogUtil.*;
import static org.apache.commons.lang3.StringUtils.*;

@Repository
public class ReceitaInsumoRepository {

    private static final Logger log = LoggerFactory.getLogger(ReceitaInsumoRepository.class);
    @PersistenceContext
    private EntityManager em;

    public ReceitaInsumoDto criarAssociacao(Long receitaId, Long insumoId, BigDecimal quantidadeUtilizadaInsumo,
                                BigDecimal valorGastoInsumo) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO tb_receita_insumo ");
            sql.append(" (insumo_id, receita_id, quantidade_utilizada_insumo, valor_gasto_insumo) ");
            sql.append(" VALUES (:insumoId, :receitaId, :quantidadeUtilizadaInsumo, :valorGastoInsumo) ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("insumoId", insumoId)
                    .setParameter("receitaId", receitaId)
                    .setParameter("quantidadeUtilizadaInsumo", quantidadeUtilizadaInsumo)
                    .setParameter("valorGastoInsumo", valorGastoInsumo);

            query.executeUpdate();

            ReceitaInsumoDto receitaInsumoAssociacaoDto = new ReceitaInsumoDto();
            receitaInsumoAssociacaoDto.setInsumoId(insumoId);
            receitaInsumoAssociacaoDto.setReceitaId(receitaId);
            receitaInsumoAssociacaoDto.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
            receitaInsumoAssociacaoDto.setValorGastoInsumo(valorGastoInsumo);

            logSucessoAoCriarAssociacaoEntreReceitaEInsumo(receitaId, insumoId);
            return receitaInsumoAssociacaoDto;

        }catch (Exception e) {
            logErroInesperadoAoCriarAssociacaoEntreReceitaEInsumo(receitaId, insumoId, e);
            throw new EmpreendedorErrorException("Erro inesperado ao criar associação entre receita e insumo.");
        }
    }

    public ReceitaInsumoDto atualizarReceitaInsumo(Long receitaId, Long insumoId,
                                                   BigDecimal quantidadeUtilizadaInsumo,
                                                   BigDecimal valorGastoInsumo) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE tb_receita_insumo ");
            sql.append(" SET quantidade_utilizada_insumo = :quantidadeUtilizadaInsumo, ");
            sql.append(" valor_gasto_insumo = :valorGastoInsumo ");
            sql.append(" WHERE receita_id = :receitaId  AND insumo_id = :insumoId LIMIT 1");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("quantidadeUtilizadaInsumo", quantidadeUtilizadaInsumo)
                    .setParameter("valorGastoInsumo", valorGastoInsumo)
                    .setParameter("receitaId", receitaId)
                    .setParameter("insumoId", insumoId);

            query.executeUpdate();

            ReceitaInsumoDto informacaoAssociacaoAtualizada = new ReceitaInsumoDto();
            informacaoAssociacaoAtualizada.setReceitaId(receitaId);
            informacaoAssociacaoAtualizada.setInsumoId(insumoId);
            informacaoAssociacaoAtualizada.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
            informacaoAssociacaoAtualizada.setValorGastoInsumo(valorGastoInsumo);

            logSucessoAoAtualizarReceitaInsumo(receitaId, insumoId);
            return informacaoAssociacaoAtualizada;

        }catch (Exception e) {
            logErroInesperadoAoAtualizarReceitaInsumo(receitaId, insumoId, e);
            throw new EmpreendedorErrorException(String
                    .format("Erro ao atualizar informações sobre a associação entre receita %d e insumo %d.", receitaId, insumoId));
        }
    }

    public Boolean verificarExistenciaDaAssociacaoDaReceitaEInsumo(Long receitaId, Long insumoId) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT 1 FROM tb_receita_insumo WHERE receita_id = :receitaId ");
            sql.append(" AND insumo_id = :insumoId ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("receitaId", receitaId)
                    .setParameter("insumoId", insumoId);

            List<?> listaDeResultado = query.getResultList();

            logSucessoAoVerificarExistenciaDaAssociacaoDeReceitaEInsumo(receitaId, insumoId);
            return !listaDeResultado.isEmpty();

        } catch (Exception e) {
            logErroInesperadoAoVerificarExistenciaDaAssociacaoDeReceitaEInsumo(receitaId, insumoId, e);
            throw new EmpreendedorErrorException(String
                    .format("Erro inesperado ao verificar existência da associação entre receita %d e insumo %d.", receitaId, insumoId));
        }
    }

    public AssociacaoDto obterTodosOsDadosDaAssociacaoPorReceitaIdEInsumoId(Long receitaId, Long insumoId) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ri.insumo_id, ri.receita_id, ri.quantidade_utilizada_insumo, ri.valor_gasto_insumo, ");
            sql.append(" r.id, r.nome, r.total_gasto_insumos, ");
            sql.append(" i.id, i.nome, i.quantidade_por_pacote, i.valor_pago_por_pacote ");
            sql.append(" FROM tb_receita_insumo AS ri ");
            sql.append(" JOIN tb_receitas AS r ON ri.receita_id = r.id ");
            sql.append(" JOIN tb_insumos AS i ON ri.insumo_id = i.id ");
            sql.append(" WHERE ri.insumo_id = :insumoId AND ri.receita_id = :receitaId LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("insumoId", insumoId)
                    .setParameter("receitaId", receitaId);

            List<Object[]> listaDeResultados = query.getResultList();
            if (listaDeResultados.isEmpty()) {
                throw new EmpreendedorErrorException(String
                        .format("Nenhuma associação entre receita %d e insumo %d foi encontada.", receitaId, insumoId));
            }

            Object[] resultado = listaDeResultados.get(0);

            AssociacaoDto associacaoEncontrada = new AssociacaoDto();
            ReceitaInsumoDto receitaInsumoDto = new ReceitaInsumoDto();
            ReceitaDto receitaDto = new ReceitaDto();
            InsumoDto insumoDto = new InsumoDto();

            receitaInsumoDto.setInsumoId(((Number) resultado[0]).longValue());
            receitaInsumoDto.setReceitaId(((Number) resultado[1]).longValue());
            BigDecimal quantidadeUtilizadaInsumo = new BigDecimal(resultado[2].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            receitaInsumoDto.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
            BigDecimal valorGastoInsumo = new BigDecimal(resultado[3].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            receitaInsumoDto.setValorGastoInsumo(valorGastoInsumo);

            receitaDto.setId(((Number) resultado[4]).longValue());
            receitaDto.setNome((String) resultado[5]);
            BigDecimal totalGastoInsumos = new BigDecimal(resultado[6].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            receitaDto.setTotalGastoInsumos(totalGastoInsumos);

            insumoDto.setId(((Number) resultado[7]).longValue());
            insumoDto.setNome((String) resultado[8]);
            insumoDto.setQuantidadePorPacote(((Number) resultado[9]).doubleValue());
            BigDecimal valorPagoPorPacote = new BigDecimal(resultado[10].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            insumoDto.setValorPagoPorPacote(valorPagoPorPacote);

            associacaoEncontrada.setReceitaInsumoDto(receitaInsumoDto);
            associacaoEncontrada.setReceitaDto(receitaDto);
            associacaoEncontrada.setInsumoDto(insumoDto);

            logSucessoAoObterTodosOsDadosDaAssociacao(receitaId, insumoId);
            return associacaoEncontrada;

        } catch (EmpreendedorErrorException e) {
            throw e;
        } catch (Exception e) {
            logErroInesperadoAoObterTodosOsDadosDaAssociacao(receitaId, insumoId, e);
            throw new EmpreendedorErrorException(String
                    .format("Erro inesperado ao obter todos dados da associacao entre receita %d e insumo %d.", receitaId, insumoId));
        }
    }

    public List<AssociacaoDto> obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(Long insumoId) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT ri.insumo_id, ri.receita_id, ri.quantidade_utilizada_insumo, ri.valor_gasto_insumo, ");
            sql.append(" r.id, r.nome, r.total_gasto_insumos ");
            sql.append(" FROM tb_receita_insumo AS ri ");
            sql.append(" JOIN tb_receitas AS r ON ri.receita_id = r.id ");
            sql.append(" WHERE ri.insumo_id = :insumoId ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("insumoId", insumoId);

            List<Object[]> listaDeResultados = query.getResultList();

            List<AssociacaoDto> listaDeAssociacoesEReceitasEncontradas = new ArrayList<>();
            for (Object[] resultado : listaDeResultados) {

                AssociacaoDto associacaoEncontrada = new AssociacaoDto();
                ReceitaInsumoDto receitaInsumoDto = new ReceitaInsumoDto();
                ReceitaDto receitaDto = new ReceitaDto();

                receitaInsumoDto.setInsumoId(((Number) resultado[0]).longValue());
                receitaInsumoDto.setReceitaId(((Number) resultado[1]).longValue());
                BigDecimal quantidadeUtilizadaInsumo = new BigDecimal(resultado[2].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaInsumoDto.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
                BigDecimal valorGastoInsumo = new BigDecimal(resultado[3].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaInsumoDto.setValorGastoInsumo(valorGastoInsumo);


                receitaDto.setId(((Number) resultado[4]).longValue());
                receitaDto.setNome((String) resultado[5]);
                BigDecimal totalGastoInsumos = new BigDecimal(resultado[6].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaDto.setTotalGastoInsumos(totalGastoInsumos);

                associacaoEncontrada.setReceitaInsumoDto(receitaInsumoDto);
                associacaoEncontrada.setReceitaDto(receitaDto);

                listaDeAssociacoesEReceitasEncontradas.add(associacaoEncontrada);
            }

            logSucessoAoObterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(insumoId);
            return listaDeAssociacoesEReceitasEncontradas;

        } catch (Exception e) {
            logErroInesperadoAoObterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(insumoId, e);
            throw new EmpreendedorErrorException(String
                    .format("Erro inesperado ao obter lista de associações e receitas relacionadas ao insumo %d.", insumoId));
        }
    }

    public ReceitaInsumoDto obterAssociacaoPorReceitaIdEInsumoId(Long receitaId, Long insumoId) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT insumo_id, receita_id, quantidade_utilizada_insumo, valor_gasto_insumo ");
            sql.append(" FROM tb_receita_insumo ");
            sql.append(" WHERE receita_id = :receitaId AND insumo_id = :insumoId LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("receitaId", receitaId)
                    .setParameter("insumoId", insumoId);

            List<Object[]> listaDeResultados = query.getResultList();
            if (listaDeResultados.isEmpty()) {
                throw new EmpreendedorErrorException(String
                        .format("Nenhuma associação entre receita %d e insumo %d foi encontada.", receitaId, insumoId));
            }

            Object[] resultado = listaDeResultados.get(0);

            ReceitaInsumoDto associacaoEncontrada = new ReceitaInsumoDto();
            associacaoEncontrada.setInsumoId(((Number) resultado[0]).longValue());
            associacaoEncontrada.setReceitaId(((Number) resultado[1]).longValue());

            BigDecimal quantidadeUtilizadaInsumo = new BigDecimal(resultado[2].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            associacaoEncontrada.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);

            BigDecimal valorGastoInsumo =  new BigDecimal(resultado[3].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            associacaoEncontrada.setValorGastoInsumo(valorGastoInsumo);

            return associacaoEncontrada;

        } catch (EmpreendedorErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new EmpreendedorErrorException(String
                    .format("Erro inesperado ao obter associação entre a receita %d e insumo %d.", receitaId, insumoId));
        }
    }

    public List<ReceitaInsumoDto> obterListaDeInsumosAssociadosAReceitasPeloId(Long insumoId) {
        try{
            String sql = " SELECT * FROM tb_receita_insumo WHERE insumo_id = :insumoId ";

            Query query = em.createNativeQuery(sql)
                    .setParameter("insumoId", insumoId);

            List<Object[]> listaDeResultados = query.getResultList();

            List<ReceitaInsumoDto> listaDeInsumosAssociadosAReceitas = new ArrayList<>();

            for (Object[] resultado : listaDeResultados) {
                ReceitaInsumoDto receitaInsumoDto = new ReceitaInsumoDto();
                receitaInsumoDto.setInsumoId(((Number) resultado[0]).longValue());
                receitaInsumoDto.setReceitaId(((Number) resultado[1]).longValue());

                BigDecimal quantidadeUtilizadaInsumo = new BigDecimal(resultado[2].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaInsumoDto.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);

                BigDecimal valorGastoInsumo = new BigDecimal(resultado[3].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaInsumoDto.setValorGastoInsumo(valorGastoInsumo);

                listaDeInsumosAssociadosAReceitas.add(receitaInsumoDto);
            }

            logSucessoAoObterListaDeInsumosAssociadosAReceitasPeloId(insumoId);
            return listaDeInsumosAssociadosAReceitas;

        } catch (Exception e) {
            logErroInesperadoAoObterListaDeInsumosAssociadosAReceitasPeloId(insumoId, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter lista de insumos associados à receitas pelo id.");
        }
    }

    public GlobalPageDto<ReceitaInsumoDto> obterListaFiltradaEPaginadaDeInsumosAssociadosAReceita(Long receitaId,
                                                                                                    String nomeInsumo,
                                                                                                    Integer paginaAtual,
                                                                                                    Integer itensPorPagina,
                                                                                                    String direcao,
                                                                                                    String ordenarPor) {
        Long totalDeInsumosAssociadosAReceita =
                obterTotalDeInsumosAssociadosAReceitaQuery(receitaId, nomeInsumo);

        List<ReceitaInsumoDto> listaDeInsumosAssociadosAReceitaFiltradosEPaginados =
                obterListaFiltradaEPaginadaDeInsumosAssociadosAReceitaQuery(receitaId, nomeInsumo,
                        paginaAtual,itensPorPagina, direcao, ordenarPor);

        GlobalPageDto<ReceitaInsumoDto> insumosAssociadosAReceitaPaginaveis =
                new GlobalPageDto<>(listaDeInsumosAssociadosAReceitaFiltradosEPaginados,
                        totalDeInsumosAssociadosAReceita,
                        paginaAtual, itensPorPagina);

        return insumosAssociadosAReceitaPaginaveis;
    }

    public Long obterTotalDeInsumosAssociadosAReceitaQuery(Long receitaId, String nomeInsumo) {
        try{
            Map<String, Object> parametros = new HashMap<>();
            StringBuilder sql = new StringBuilder();

            sql.append(" SELECT COUNT(*) FROM tb_receita_insumo AS ri ");
            sql.append(" JOIN tb_insumos AS i ON ri.insumo_id = i.id ");
            sql.append(" WHERE 1=1 AND ri.receita_id = :receitaId ");

            if (isNotBlank(nomeInsumo)) {
                sql.append(" AND LOWER(i.nome) LIKE LOWER(:nomeInsumo) ");
                parametros.put("nomeInsumo", "%" + nomeInsumo + "%");
            }

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("receitaId", receitaId);

            setQueryParameters(parametros, query);

            Object resultado = query.getSingleResult();
            Number totalDeItens = (Number) resultado;

            logSucessoAoObterTotalDeInsumosAssociadosAReceita(receitaId);
            return totalDeItens.longValue();

        } catch (Exception e) {
            logErroInesperadoAoObterTotalDeInsumosAssociadosAReceita(receitaId, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter total de insumos associados à receita informada.");
        }
    }

    private List<ReceitaInsumoDto> obterListaFiltradaEPaginadaDeInsumosAssociadosAReceitaQuery(Long receitaId,
                                                                                                 String nomeInsumo,
                                                                                                 Integer paginaAtual,
                                                                                                 Integer itensPorPagina,
                                                                                                 String direcao,
                                                                                                 String ordenarPor) {
        try {
            Map<String, Object> parametros = new HashMap<>();

            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT i.nome, ri.quantidade_utilizada_insumo, ri.valor_gasto_insumo ");
            sql.append(" FROM tb_receita_insumo AS ri ");
            sql.append(" JOIN tb_insumos AS i ON ri.insumo_id = i.id ");
            sql.append(" WHERE 1=1 AND ri.receita_id = :receitaId ");

            if (isNotBlank(nomeInsumo)) {
                sql.append(" AND LOWER(i.nome) LIKE LOWER(:nomeInsumo) ");
                parametros.put("nomeInsumo", "%" + nomeInsumo + "%");
            }

            sql.append(" ORDER BY ").append(ordenarPor).append(" ").append(direcao).append(" ");
            sql.append(" LIMIT :limit OFFSET :offset ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("receitaId", receitaId)
                    .setParameter("limit", itensPorPagina)
                    .setParameter("offset", paginaAtual * itensPorPagina);

            setQueryParameters(parametros, query);

            List<Object[]> listaDeResultados = query.getResultList();
            List<ReceitaInsumoDto> listaDeInsumosAssociadosAReceita = new ArrayList<>();

            for (Object[] resultado : listaDeResultados) {
                ReceitaInsumoDto receitaInsumoDto = new ReceitaInsumoDto();

                receitaInsumoDto.setInsumoNome((String) resultado[0]);

                BigDecimal quantidadeUtilizadaInsumo =  new BigDecimal(resultado[1].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaInsumoDto.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);

                BigDecimal valorGastoInsumo = new BigDecimal(resultado[2].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaInsumoDto.setValorGastoInsumo(valorGastoInsumo);

                listaDeInsumosAssociadosAReceita.add(receitaInsumoDto);
            }

            logSucessoAoObterListaFiltradaEPaginadaDeInsumosAssociadosAReceita(receitaId);
            return listaDeInsumosAssociadosAReceita;

        } catch (Exception e) {
            logErroInesperadoAoObterListaFiltradaEPaginadaDeInsumosAssociadosAReceita(receitaId, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter lista de insumos associados à receita informada.");
        }
    }

    private void setQueryParameters(Map<String, Object> parameters, Query query) {
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
    }
}
