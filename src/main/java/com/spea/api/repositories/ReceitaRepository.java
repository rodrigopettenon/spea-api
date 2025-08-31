package com.spea.api.repositories;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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

    public List<ReceitaDto> obterListaDeReceitas() {
        try{
            String sql = " SELECT id, nome, total_gasto_insumos FROM tb_receitas ";

            Query query = em.createNativeQuery(sql);

            List<Object[]> listaDeResultados =  query.getResultList();

            List<ReceitaDto> listaDeReceitas = new ArrayList<>();
            for (Object[] resultado : listaDeResultados) {
                ReceitaDto receitaDto = new ReceitaDto();

                receitaDto.setId(((Number) resultado[0]).longValue());
                receitaDto.setNome((String) resultado[1]);
                BigDecimal totalGastoInsumos = new BigDecimal(resultado[2].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                receitaDto.setTotalGastoInsumos(totalGastoInsumos);

                listaDeReceitas.add(receitaDto);
            }

            logSucessoAoObterListaDeReceitas();
            return listaDeReceitas;
        } catch (Exception e) {
            logErroInesperadoAoObterListaDeReceitas(e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter a lista de receitas.");
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
}
