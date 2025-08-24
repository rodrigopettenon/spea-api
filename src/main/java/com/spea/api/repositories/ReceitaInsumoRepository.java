package com.spea.api.repositories;

import com.spea.api.dtos.ReceitaInsumoDto;
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
import java.util.List;

import static com.spea.api.utils.LogUtil.*;

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
}
