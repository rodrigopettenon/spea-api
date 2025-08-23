package com.spea.api.repositories;

import com.spea.api.dtos.InsumoDto;
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
public class InsumoRepository {

    @PersistenceContext
    private EntityManager em;

    public InsumoDto cadastrarInsumo(InsumoDto insumoDto) {
        try {
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

    public List<InsumoDto> obterListaDeInsumos() {
        try {
            String sql = " SELECT id, nome, quantidade_por_pacote, valor_pago_por_pacote FROM tb_insumos ";

            Query query = em.createNativeQuery(sql);

            List<Object[]> listaDeResultados =  query.getResultList();

            List<InsumoDto> listaDeInsumos = new ArrayList<>();
            for (Object[] resultado : listaDeResultados) {
                InsumoDto insumoDto = new InsumoDto();

                insumoDto.setId(((Number) resultado[0]).longValue());
                insumoDto.setNome((String) resultado[1]);
                insumoDto.setQuantidadePorPacote(((Number) resultado[2]).doubleValue());

                BigDecimal valor = new BigDecimal(resultado[3].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                insumoDto.setValorPagoPorPacote(valor);

                listaDeInsumos.add(insumoDto);
            }

            logSucessoAoObterListaDeInsumos();
            return listaDeInsumos;

        }catch (Exception e) {
            logErroInesperadoAoObterListaDeInsumos(e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter lista de insumos.");
        }
    }

    public InsumoDto obterInsumoPeloId(Long id) {
        try{
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT id, nome, quantidade_por_pacote, valor_pago_por_pacote FROM tb_insumos ");
            sql.append(" WHERE id = :id LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("id", id);

            List<Object[]> listaDeResultados = query.getResultList();

            if(listaDeResultados.isEmpty()) {
                throw new EmpreendedorErrorException("Nenhum insumo encontrado pelo id informado.");
            }

            Object[] resultado = listaDeResultados.get(0);

            InsumoDto insumoEncontradoDto = new InsumoDto();
            insumoEncontradoDto.setId(((Number) resultado[0]).longValue());
            insumoEncontradoDto.setNome((String) resultado[1]);
            insumoEncontradoDto.setQuantidadePorPacote(((Number) resultado[2]).doubleValue());

            BigDecimal valorPagoPorPacote = new BigDecimal(resultado[3].toString())
                    .setScale(2, RoundingMode.HALF_EVEN);
            insumoEncontradoDto.setValorPagoPorPacote(valorPagoPorPacote);

            logSucessoAoObterInsumoPeloId(id);
            return insumoEncontradoDto;

        }catch (EmpreendedorErrorException e) {
            throw e;
        }
        catch (Exception e) {
            logErroInesperadoAoObterInsumoPeloId(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter insumo pelo id.");
        }
    }

    public Boolean verificarExistenciaDoInsumoPeloId(Long id) {
        try {
            String sql = " SELECT 1 FROM tb_insumos WHERE id = :id LIMIT 1 ";

            Query query = em.createNativeQuery(sql)
                    .setParameter("id", id);

            List<?> listaDeResultados = query.getResultList();

            logSucessoAoVerificarExistenciaDoInsumo(id);
            return !listaDeResultados.isEmpty();

        } catch (Exception e) {
            logErroInesperadoAoVerificarExistenciaDoInsumo(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao verificar existência do insumo pelo id.");
        }
    }

    public InsumoDto atualizarInsumo(Long id, InsumoDto insumoDto) {
        try {
            StringBuilder sql = new StringBuilder();

            sql.append(" UPDATE tb_insumos ");
            sql.append(" SET nome = :nome, ");
            sql.append(" quantidade_por_pacote = :quantidadePorPacote, ");
            sql.append(" valor_pago_por_pacote = :valorPagoPorPacote ");
            sql.append(" WHERE id = :id LIMIT 1 ");

            Query query = em.createNativeQuery(sql.toString())
                    .setParameter("nome", insumoDto.getNome())
                    .setParameter("quantidadePorPacote", insumoDto.getQuantidadePorPacote())
                    .setParameter("valorPagoPorPacote", insumoDto.getValorPagoPorPacote())
                    .setParameter("id", id);

            query.executeUpdate();

            InsumoDto insumoDtoAtualizado = new InsumoDto();
            insumoDtoAtualizado.setId(id);
            insumoDtoAtualizado.setNome(insumoDto.getNome());
            insumoDtoAtualizado.setQuantidadePorPacote(insumoDto.getQuantidadePorPacote());
            insumoDtoAtualizado.setValorPagoPorPacote(insumoDto.getValorPagoPorPacote());

            logSucessoAoAtualizarInsumo(id);
            return insumoDtoAtualizado;
        } catch (Exception e) {
            logErroInesperadoAoAtualizarInsumo(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao realizar atualização do insumo.");
        }
    }

    public void deletarInsumo(Long id) {
        try {
            String sql = " DELETE FROM tb_insumos WHERE id = :id ";

            Query query = em.createNativeQuery(sql)
                    .setParameter("id", id);

            query.executeUpdate();
            logSucessoAoDeletarInsumo(id);
        } catch (Exception e) {
            logErroInesperadoAoDeletarInsumo(id, e);
            throw new EmpreendedorErrorException("Erro inesperado ao deletar insumo pelo ID");
        }
    }
}
