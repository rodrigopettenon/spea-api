package com.spea.api.repositories;

import com.spea.api.dtos.GlobalPageDto;
import com.spea.api.dtos.InsumoDto;
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

    public GlobalPageDto<InsumoDto> obterListaFiltradaEPaginadaDeInsumos(String nome, Integer paginaAtual,
                                                                         Integer itensPorPagina,
                                                                         String direcao, String ordenarPor) {
        Long totalDeInsumos = obterTotalDeInsumosFiltradosQuery(nome);

        List<InsumoDto> listaDeInsumosFiltradaOrdenadaEPaginada =
                obterListaFiltradaEPaginadaDeInsumosQuery(nome, paginaAtual, itensPorPagina,
                direcao, ordenarPor);

        GlobalPageDto<InsumoDto> listaFiltradaEPaginadaDeInsumos =
                new GlobalPageDto<>(listaDeInsumosFiltradaOrdenadaEPaginada,
                        totalDeInsumos, paginaAtual, itensPorPagina);

        return listaFiltradaEPaginadaDeInsumos;
    }

    public Long obterTotalDeInsumosFiltradosQuery(String nome) {
        try{
            Map<String, Object> parametros = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT COUNT(*) FROM tb_insumos ");

            if (isNotBlank(nome)) {
                sql.append(" WHERE LOWER(nome) LIKE LOWER(:nome) ");
                parametros.put("nome", "%" + nome + "%");
            }

            Query query = em.createNativeQuery(sql.toString());

            setQueryParameters(parametros, query);

            Object resultado = query.getSingleResult();
            Number totalDeInsumos = (Number) resultado;

            logSucessoAoObterTotalDeInsumosFiltrados(nome);
            return totalDeInsumos.longValue();

        } catch (Exception e) {
            logErroInesperadoAoObterTotalDeInsumosFiltrados(nome, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter total de insumos filtrados.");
        }
    }

    private List<InsumoDto> obterListaFiltradaEPaginadaDeInsumosQuery(String nome, Integer paginaAtual,
                                                                      Integer itensPorPagina, String direcao,
                                                                      String ordenarPor) {
        try{
            Map<String, Object> parametros = new HashMap<>();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT id, nome, quantidade_por_pacote, valor_pago_por_pacote FROM tb_insumos ");

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
            List<InsumoDto> listaFiltradaEPaginadaDeInsumos = new ArrayList<>();

            for (Object[] resultado : listaDeResultados) {
                InsumoDto insumoDto = new InsumoDto();

                insumoDto.setId(((Number) resultado[0]).longValue());
                insumoDto.setNome((String) resultado[1]);
                insumoDto.setQuantidadePorPacote(((Number) resultado[2]).doubleValue());

                BigDecimal valorPagoPorPacote = new BigDecimal(resultado[3].toString())
                        .setScale(2, RoundingMode.HALF_EVEN);
                insumoDto.setValorPagoPorPacote(valorPagoPorPacote);

                listaFiltradaEPaginadaDeInsumos.add(insumoDto);
            }
            logSucessoAoObterListaDeInsumosFiltradosEPaginados(nome);
            return listaFiltradaEPaginadaDeInsumos;

        }catch (Exception e) {
            logErroInesperadoAoObterListaDeInsumosFiltradosEPaginados(nome, e);
            throw new EmpreendedorErrorException("Erro inesperado ao obter lista de insumos filtrada e paginada.");
        }
    }

    private void setQueryParameters(Map<String, Object> parameters, Query query) {
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }
    }
}
