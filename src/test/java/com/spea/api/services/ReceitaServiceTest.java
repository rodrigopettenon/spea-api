package com.spea.api.services;

import com.spea.api.dtos.ReceitaDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.ReceitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaServiceTest {

    @InjectMocks
    private ReceitaService receitaService;

    @Mock
    private ReceitaRepository receitaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Método cadastrarReceita
    @Test
    @DisplayName("Deve cadastrar receita com sucesso quando os dados forem válidos")
    void deveCadastrarReceitaComSucesso() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Margherita");

        ReceitaDto receitaSalva = new ReceitaDto();
        receitaSalva.setId(1L);
        receitaSalva.setNome("Pizza Margherita");

        when(receitaRepository.cadastrarReceita(receitaDto)).thenReturn(receitaSalva);

        // Act
        ReceitaDto resultado = receitaService.cadastrarReceita(receitaDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Pizza Margherita", resultado.getNome());

        verify(receitaRepository).cadastrarReceita(receitaDto);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome da receita for nulo")
    void deveLancarExcecaoQuandoNomeForNulo() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome(null);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaService.cadastrarReceita(receitaDto));

        assertEquals("O nome da receita é obrigatório", excecao.getMessage());
        verify(receitaRepository, never()).cadastrarReceita(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome da receita for vazio")
    void deveLancarExcecaoQuandoNomeForVazio() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("   ");

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaService.cadastrarReceita(receitaDto));

        assertEquals("O nome da receita é obrigatório", excecao.getMessage());
        verify(receitaRepository, never()).cadastrarReceita(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome da receita exceder 100 caracteres")
    void deveLancarExcecaoQuandoNomeExcederTamanhoMaximo() {
        // Arrange
        String nomeLongo = "Receita super mega hiper ultra deluxe com todos os ingredientes especiais e " +
                "molho secreto da casa plus premium gourmet edition limited";

        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome(nomeLongo);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaService.cadastrarReceita(receitaDto));

        assertEquals("O nome da receita deve ter no máximo 100 caracteres", excecao.getMessage());
        verify(receitaRepository, never()).cadastrarReceita(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer erro no repositório")
    void deveLancarExcecaoQuandoRepositorioLancarErro() {
        // Arrange
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Calabresa");

        when(receitaRepository.cadastrarReceita(receitaDto))
                .thenThrow(new RuntimeException("Erro no banco de dados"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> receitaService.cadastrarReceita(receitaDto));

        verify(receitaRepository).cadastrarReceita(receitaDto);
    }

    // Método atualizarReceita
    @Test
    @DisplayName("Deve atualizar receita com sucesso quando dados forem válidos")
    void deveAtualizarReceitaComSucesso() {
        // Arrange
        Long id = 1L;
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Margherita Atualizada");

        ReceitaDto receitaAtualizada = new ReceitaDto();
        receitaAtualizada.setId(id);
        receitaAtualizada.setNome("Pizza Margherita Atualizada");

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(id)).thenReturn(true);
        when(receitaRepository.atualizarReceita(id, receitaDto)).thenReturn(receitaAtualizada);

        // Act
        ReceitaDto resultado = receitaService.atualizarReceita(id, receitaDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Pizza Margherita Atualizada", resultado.getNome());

        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(id);
        verify(receitaRepository).atualizarReceita(id, receitaDto);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID da receita não existir")
    void deveLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        Long id = 999L;
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome("Pizza Inexistente");

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(id)).thenReturn(false);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaService.atualizarReceita(id, receitaDto));

        assertEquals("O ID da receita informado não está cadastrado.", excecao.getMessage());
        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(id);
        verify(receitaRepository, never()).atualizarReceita(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome da receita for inválido")
    void deveLancarExcecaoQuandoNomeForInvalido() {
        // Arrange
        Long id = 1L;
        ReceitaDto receitaDto = new ReceitaDto();
        receitaDto.setNome(" "); // Nome inválido

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaService.atualizarReceita(id, receitaDto));

        assertEquals("O nome da receita é obrigatório", excecao.getMessage());
        verify(receitaRepository, never()).atualizarReceita(any(), any());
    }


}