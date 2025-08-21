package com.spea.api.repositories;

import com.spea.api.dtos.ReceitaInsumoDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaInsumoRepositoryTest {

    @InjectMocks
    private ReceitaInsumoRepository receitaInsumoRepository;

    @Mock
    private EntityManager em;

    @Mock
    private Query query;

    @Test
    @DisplayName("Deve criar associação entre receita e insumo com sucesso")
    void deveCriarAssociacaoComSucesso() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");
        BigDecimal valorGastoInsumo = new BigDecimal("5.02");

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("insumoId"), eq(insumoId))).thenReturn(query);
        when(query.setParameter(eq("receitaId"), eq(receitaId))).thenReturn(query);
        when(query.setParameter(eq("quantidadeUtilizadaInsumo"), eq(quantidadeUtilizadaInsumo))).thenReturn(query);
        when(query.setParameter(eq("valorGastoInsumo"), eq(valorGastoInsumo))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Execução
        ReceitaInsumoDto resultado = receitaInsumoRepository.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, valorGastoInsumo);

        // Verificações
        assertNotNull(resultado);
        assertEquals(receitaId, resultado.getReceitaId());
        assertEquals(insumoId, resultado.getInsumoId());
        assertEquals(quantidadeUtilizadaInsumo, resultado.getQuantidadeUtilizadaInsumo());
        assertEquals(valorGastoInsumo, resultado.getValorGastoInsumo());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("insumoId", insumoId);
        verify(query).setParameter("receitaId", receitaId);
        verify(query).setParameter("quantidadeUtilizadaInsumo", quantidadeUtilizadaInsumo);
        verify(query).setParameter("valorGastoInsumo", valorGastoInsumo);
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro inesperado ao criar associação")
    void deveLancarExcecaoQuandoOcorrerErroAoCriarAssociacao() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");
        BigDecimal valorGastoInsumo = new BigDecimal("5.02");

        // Configuração do mock para lançar exceção
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Erro simulado"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            receitaInsumoRepository.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, valorGastoInsumo);
        });

        assertEquals("Erro inesperado ao criar associação entre receita e insumo.", excecao.getMessage());

        verify(em).createNativeQuery(anyString());
        verify(query, times(4)).setParameter(anyString(), any());
        verify(query).executeUpdate();
    }

    // Método verificarExistenciaDaAssociacaoDaReceitaEInsumo

    @Test
    @DisplayName("Deve retornar true quando existir associação entre receita e insumo")
    void deveRetornarTrueQuandoExistirAssociacao() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        List<?> listaComResultado = Collections.singletonList(new Object[]{1});

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("receitaId"), eq(receitaId))).thenReturn(query);
        when(query.setParameter(eq("insumoId"), eq(insumoId))).thenReturn(query);
        when(query.getResultList()).thenReturn(listaComResultado);

        // Execução
        Boolean resultado = receitaInsumoRepository.verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId);

        // Verificações
        assertTrue(resultado);
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("receitaId", receitaId);
        verify(query).setParameter("insumoId", insumoId);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve retornar false quando não existir associação entre receita e insumo")
    void deveRetornarFalseQuandoNaoExistirAssociacao() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 3L;
        List<?> listaVazia = new ArrayList<>();

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("receitaId"), eq(receitaId))).thenReturn(query);
        when(query.setParameter(eq("insumoId"), eq(insumoId))).thenReturn(query);
        when(query.getResultList()).thenReturn(listaVazia);

        // Execução
        Boolean resultado = receitaInsumoRepository.verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId);

        // Verificações
        assertFalse(resultado);
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("receitaId", receitaId);
        verify(query).setParameter("insumoId", insumoId);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro inesperado ao verificar existência da associação")
    void deveLancarExcecaoQuandoOcorrerErroAoVerificarExistenciaDaAssociacao() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;

        // Configuração do mock para lançar exceção
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("receitaId"), eq(receitaId))).thenReturn(query);
        when(query.setParameter(eq("insumoId"), eq(insumoId))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Erro simulado"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            receitaInsumoRepository.verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId);
        });

        assertEquals("Erro inesperado ao verificar existência da associação entre receita e insumo.", excecao.getMessage());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("receitaId", receitaId);
        verify(query).setParameter("insumoId", insumoId);
        verify(query).getResultList();
    }

}