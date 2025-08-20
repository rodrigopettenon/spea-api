package com.spea.api.repositories;

import com.spea.api.dtos.ReceitaInsumoDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class ReceitaInsumoRepository {

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

            return receitaInsumoAssociacaoDto;

        }catch (Exception e) {
            throw new EmpreendedorErrorException("Erro inesperado ao criar associação entre receita e insumo.");
        }
    }
}
