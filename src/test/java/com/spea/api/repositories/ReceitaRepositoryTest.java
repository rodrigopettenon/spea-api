package com.spea.api.repositories;

import com.spea.api.dtos.ReceitaDto;
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
class ReceitaRepositoryTest {

    @InjectMocks
    private ReceitaRepository receitaRepository;

    @Mock
    private EntityManager em;

    @Mock
    private Query query;

    // Método cadastrarReceita
    @Test
    @DisplayName("Deve cadastrar receita com sucesso")
    void deveCadastrarReceitaComSucesso() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Margherita");

        String sqlEsperada = " INSERT INTO tb_receitas (nome) VALUES (:nome) ";

        when(em.createNativeQuery(sqlEsperada)).thenReturn(query);
        when(query.setParameter("nome", receitaDto.getNome())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        ReceitaDto resultado = receitaRepository.cadastrarReceita(receitaDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(receitaDto.getNome(), resultado.getNome());

        verify(em).createNativeQuery(sqlEsperada);
        verify(query).setParameter("nome", receitaDto.getNome());
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao cadastrar receita")
    void deveLancarExcecaoQuandoOcorrerErroAoCadastrar() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Calabresa");
        RuntimeException exceptionSimulada = new RuntimeException("Erro de banco de dados");

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("nome", receitaDto.getNome())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(exceptionSimulada);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaRepository.cadastrarReceita(receitaDto));

        assertEquals("Erro inesperado ao cadastrar receita.", excecao.getMessage());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("nome", receitaDto.getNome());
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve retornar o DTO da receita cadastrada")
    void deveRetornarODtoDaReceitaCadastrada() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Lasanha Bolonhesa");

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        ReceitaDto resultado = receitaRepository.cadastrarReceita(receitaDto);

        // Assert
        assertSame(receitaDto, resultado);
        verify(em).createNativeQuery(anyString());
    }

    // Método atualizarReceita
    @Test
    @DisplayName("Deve atualizar receita com sucesso")
    void deveAtualizarReceitaComSucesso() {
        // Arrange
        Long id = 1L;
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Atualizada");
        receitaDto.setTotalGastoInsumos(new BigDecimal("25.50"));

        String sqlEsperada = " UPDATE tb_receitas  SET nome = :nome,  total_gasto_insumos = :total_gasto_insumos  WHERE id = :id LIMIT 1 ";

        when(em.createNativeQuery(sqlEsperada)).thenReturn(query);
        when(query.setParameter("nome", receitaDto.getNome())).thenReturn(query);
        when(query.setParameter("total_gasto_insumos", receitaDto.getTotalGastoInsumos())).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        ReceitaDto resultado = receitaRepository.atualizarReceita(id, receitaDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(receitaDto.getNome(), resultado.getNome());
        assertEquals(receitaDto.getTotalGastoInsumos(), resultado.getTotalGastoInsumos());

        verify(em).createNativeQuery(sqlEsperada);
        verify(query).setParameter("nome", receitaDto.getNome());
        verify(query).setParameter("total_gasto_insumos", receitaDto.getTotalGastoInsumos());
        verify(query).setParameter("id", id);
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao atualizar receita")
    void deveLancarExcecaoQuandoOcorrerErroAoAtualizar() {
        // Arrange
        Long id = 1L;
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza");
        receitaDto.setTotalGastoInsumos(new BigDecimal("20.00"));

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenThrow(new RuntimeException("Erro de banco de dados"));

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaRepository.atualizarReceita(id, receitaDto));

        assertEquals("Erro inesperado ao atualizar receita.", excecao.getMessage());
        verify(em).createNativeQuery(anyString());
        verify(query).executeUpdate();
    }

    @Test
    @DisplayName("Deve retornar o DTO da receita atualizada")
    void deveRetornarODtoDaReceitaAtualizada() {
        // Arrange
        Long id = 1L;
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Lasanha");
        receitaDto.setTotalGastoInsumos(new BigDecimal("15.75"));

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // Act
        ReceitaDto resultado = receitaRepository.atualizarReceita(id, receitaDto);

        // Assert
        assertSame(receitaDto, resultado);
    }

    // Método verificarExistenciaDaReceitaPeloId
    @Test
    @DisplayName("Deve retornar true quando receita existir pelo ID")
    void deveRetornarTrueQuandoReceitaExistir() {
        // Arrange
        Long id = 1L;
        List<?> listaResultados = Collections.singletonList(new Object());

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getResultList()).thenReturn(listaResultados);

        // Act
        Boolean resultado = receitaRepository.verificarExistenciaDaReceitaPeloId(id);

        // Assert
        assertTrue(resultado);
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve retornar false quando receita não existir pelo ID")
    void deveRetornarFalseQuandoReceitaNaoExistir() {
        // Arrange
        Long id = 999L;
        List<?> listaResultados = Collections.emptyList();

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getResultList()).thenReturn(listaResultados);

        // Act
        Boolean resultado = receitaRepository.verificarExistenciaDaReceitaPeloId(id);

        // Assert
        assertFalse(resultado);
        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro ao verificar existência da receita")
    void deveLancarExcecaoQuandoOcorrerErroAoVerificarExistencia() {
        // Arrange
        Long id = 1L;

        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Erro de banco de dados"));

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaRepository.verificarExistenciaDaReceitaPeloId(id));

        assertEquals("Erro inesperado ao verificar existência da receita pelo id.", excecao.getMessage());
        verify(em).createNativeQuery(anyString());
        verify(query).getResultList();
    }

    //Método obterReceitaPeloId
    @Test
    @DisplayName("Deve obter receita pelo ID com sucesso")
    void deveObterReceitaPeloIdComSucesso() {
        // Arrange
        Long id = 1L;
        List<Object[]> listaDeResultados = new ArrayList<>();
        listaDeResultados.add(new Object[]{1L, "Pizza Margherita", new BigDecimal("25.50")});

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.getResultList()).thenReturn(listaDeResultados);

        // Execução
        ReceitaDto resultado = receitaRepository.obterReceitaPeloId(id);

        // Verificações
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pizza Margherita", resultado.getNome());
        assertEquals(new BigDecimal("25.50"), resultado.getTotalGastoInsumos());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhuma receita for encontrada pelo ID")
    void deveLancarExcecaoQuandoNenhumaReceitaForEncontradaPeloId() {
        // Arrange
        Long id = 999L;
        List<Object[]> listaVazia = new ArrayList<>();

        // Configuração do mock
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.getResultList()).thenReturn(listaVazia);

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            receitaRepository.obterReceitaPeloId(id);
        });

        assertEquals("Nenhuma receita encontrada pelo id informado.", excecao.getMessage());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro inesperado ao obter receita pelo ID")
    void deveLancarExcecaoQuandoOcorrerErroAoObterReceitaPeloId() {
        // Arrange
        Long id = 1L;

        // Configuração do mock para lançar exceção
        when(em.createNativeQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("id"), eq(id))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Erro simulado"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            receitaRepository.obterReceitaPeloId(id);
        });

        assertEquals("Erro inesperado ao buscar receita pelo id.", excecao.getMessage());

        verify(em).createNativeQuery(anyString());
        verify(query).setParameter("id", id);
        verify(query).getResultList();
    }

}