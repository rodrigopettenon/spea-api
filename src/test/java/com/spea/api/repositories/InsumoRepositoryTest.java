package com.spea.api.repositories;

import com.spea.api.dtos.InsumoDto;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsumoRepositoryTest {

    @InjectMocks
    private InsumoRepository insumoRepository;

    @Mock
    private EntityManager em;

    @Mock
    private Query query;

    // Método cadastrarInsumo
    @Test
    @DisplayName("Deve cadastrar um insumo com sucesso.")
    void deveCadastrarUmInsumoComSucesso() {
        String nome = "Cenoura";
        Double quatidadePorPacote = 500.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("4.00");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quatidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("nome"), eq(nome))).thenReturn(query);
        when(query.setParameter(eq("quantidade_por_pacote"), eq(quatidadePorPacote))).thenReturn(query);
        when(query.setParameter(eq("valor_pago_por_pacote"), eq(valorPagoPorPacote))).thenReturn(query);

        when(query.executeUpdate()).thenReturn(1);

        insumoRepository.cadastrarInsumo(dto);

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter(eq("nome"), eq(nome));
        verify(query).setParameter(eq("quantidade_por_pacote"), eq(quatidadePorPacote));
        verify(query).setParameter(eq("valor_pago_por_pacote"), eq(valorPagoPorPacote));
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção inesperada ao executar a query para cadastrar um novo insumo.")
    void deveOcorrerUmaExcecaoInesperadaAoExecutarAQueryParaCadastrarUmNovoInsumo() {
        String nome = "Cenoura";
        Double quatidadePorPacote = 500.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("4.00");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quatidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("nome"), eq(nome))).thenReturn(query);
        when(query.setParameter(eq("quantidade_por_pacote"), eq(quatidadePorPacote))).thenReturn(query);
        when(query.setParameter(eq("valor_pago_por_pacote"), eq(valorPagoPorPacote))).thenReturn(query);

        when(query.executeUpdate()).thenThrow(new RuntimeException("Simulação de Exceção"));

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoRepository.cadastrarInsumo(dto);
        });

        assertEquals("Erro inesperado ao cadastrar um novo insumo.", excecao.getMessage());
    }

    // Métodos obterListaDeInsumos
    @Test
    @DisplayName("Deve obter uma lista de insumos com sucesso.")
    void deveObterUmaListaDeInsumosComSucesso() {
        List<Object[]> listaDeResultados = new ArrayList<>();
        listaDeResultados.add(new Object[]{1L, "Arroz Branco", 1000.00, new BigDecimal("11.90")});

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(listaDeResultados);

        List<InsumoDto> listaDeInsumosRetornada = insumoRepository.obterListaDeInsumos();

        assertEquals(1, listaDeInsumosRetornada.size());

        assertEquals(1L, listaDeInsumosRetornada.get(0).getId());
        assertEquals("Arroz Branco", listaDeInsumosRetornada.get(0).getNome());
        assertEquals(1000.00, listaDeInsumosRetornada.get(0).getQuantidadePorPacote());
        assertEquals(new BigDecimal("11.90"), listaDeInsumosRetornada.get(0).getValorPagoPorPacote());

        verify(em).createNativeQuery(anyString());
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao obter lista de insumos.")
    void deveLancarExcecaoQuandoOcorrerErroAoObterListaDeInsumos() {
        // Configuração do mock para lançar exceção
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Erro simulado"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoRepository.obterListaDeInsumos();
        });

        assertEquals("Erro inesperado ao obter lista de insumos.", excecao.getMessage());
        verify(em).createNativeQuery(anyString());
        verify(query).getResultList();
    }

    // Método verificarExistenciaDoInsumoPeloId
    @Test
    @DisplayName("Deve retornar true quando insumo existir pelo ID.")
    void deveRetornarTrueQuandoInsumoExistirPeloId() {
        Long id = 1L;
        List<?> listaMock = Collections.singletonList(new Object[]{1});

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.getResultList()).thenReturn(listaMock);

        // Execução
        Boolean resultado = insumoRepository.verificarExistenciaDoInsumoPeloId(id);

        // Verificações
        assertTrue(resultado);
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve retornar false quando insumo não existir pelo ID.")
    void deveRetornarFalseQuandoInsumoNaoExistirPeloId() {
        Long id = 999L;
        List<?> listaMock = new ArrayList<>();

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.getResultList()).thenReturn(listaMock);

        // Execução
        Boolean resultado = insumoRepository.verificarExistenciaDoInsumoPeloId(id);

        // Verificações
        assertFalse(resultado);
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao verificar existência do insumo.")
    void deveLancarExcecaoQuandoOcorrerErroAoVerificarExistenciaDoInsumo() {
        Long id = 1L;

        // Configuração do mock para lançar exceção
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Erro simulado"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoRepository.verificarExistenciaDoInsumoPeloId(id);
        });

        assertEquals("Erro inesperado ao verificar existência do insumo pelo id.", excecao.getMessage());
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    // Método atualizarInsumo
    @Test
    @DisplayName("Deve atualizar insumo com sucesso.")
    void deveAtualizarInsumoComSucesso() {
        Long id = 1L;
        InsumoDto dto = new InsumoDto();
        dto.setNome("Farinha de Trigo Integral");
        dto.setQuantidadePorPacote(500.00);
        dto.setValorPagoPorPacote(new BigDecimal("7.50"));

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("nome"), eq(dto.getNome()))).thenReturn(query);
        when(query.setParameter(eq("quantidadePorPacote"), eq(dto.getQuantidadePorPacote()))).thenReturn(query);
        when(query.setParameter(eq("valorPagoPorPacote"), eq(dto.getValorPagoPorPacote()))).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Execução
        InsumoDto resultado = insumoRepository.atualizarInsumo(id, dto);

        // Verificações
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(dto.getNome(), resultado.getNome());
        assertEquals(dto.getQuantidadePorPacote(), resultado.getQuantidadePorPacote());
        assertEquals(dto.getValorPagoPorPacote(), resultado.getValorPagoPorPacote());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("nome", dto.getNome());
        verify(query).setParameter("quantidadePorPacote", dto.getQuantidadePorPacote());
        verify(query).setParameter("valorPagoPorPacote", dto.getValorPagoPorPacote());
        verify(query).setParameter("id", id);
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao atualizar insumo.")
    void deveLancarExcecaoQuandoOcorrerErroAoAtualizarInsumo() {
        Long id = 1L;
        InsumoDto dto = new InsumoDto();
        dto.setNome("Farinha de Trigo");
        dto.setQuantidadePorPacote(1000.00);
        dto.setValorPagoPorPacote(new BigDecimal("5.90"));

        // Configuração do mock para lançar exceção
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Erro simulado"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoRepository.atualizarInsumo(id, dto);
        });

        assertEquals("Erro inesperado ao realizar atualização do insumo.", excecao.getMessage());
        verify(em).createNativeQuery(anyString());
        verify(query).executeUpdate();
    }
}
