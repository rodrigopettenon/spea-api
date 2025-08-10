package com.spea.api.repositories;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spea.api.utils.LogUtil.*;

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

    public ReceitaDto atualizarReceita(Long id, ReceitaDto receitaDto) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE tb_receitas ");
            sql.append(" SET nome = :nome, ");
            sql.append(" total_gasto_insumos = :total_gasto_insumos ");
            sql.append(" WHERE id = :id LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("nome", receitaDto.getNome())
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
}
