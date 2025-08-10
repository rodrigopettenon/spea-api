package com.spea.api.repositories;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import static com.spea.api.utils.LogUtil.*;

@Repository
public class ReceitaRepository {

    @PersistenceContext
    private EntityManager em;


    public ReceitaDto cadastrarReceita(ReceitaDto receitaDto) {
        try{
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
}
