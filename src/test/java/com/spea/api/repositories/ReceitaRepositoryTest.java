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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
}