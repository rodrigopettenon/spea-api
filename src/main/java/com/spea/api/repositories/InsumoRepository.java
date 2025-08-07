package com.spea.api.repositories;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import static com.spea.api.utils.LogUtil.*;

@Repository
public class InsumoRepository {

    @PersistenceContext
    private EntityManager em;

    public InsumoDto cadastrarInsumo(InsumoDto insumoDto) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO tb_insumos (nome, quantidade_por_pacote, valor_pago_por_pacote) ");
            sql.append(" VALUES (:nome, :quantidade_por_pacote, :valor_pago_por_pacote) ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("nome", insumoDto.getNome())
                    .setParameter("quantidade_por_pacote", insumoDto.getQuantidadePorPacote())
                    .setParameter("valor_pago_por_pacote", insumoDto.getValorPagoPorPacote());

            query.executeUpdate();
            logSucessoAoCadastrarInsumo(insumoDto.getNome());

            return insumoDto;
        }catch (Exception e) {
            logErroInesperadoAoCadastrarInsumo(insumoDto.getNome(), e);
            throw new EmpreendedorErrorException("Erro inesperado ao cadastrar um novo insumo.");
        }
    }
}
