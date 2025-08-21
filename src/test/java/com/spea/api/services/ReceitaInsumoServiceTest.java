package com.spea.api.services;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.dtos.ReceitaDto;
import com.spea.api.dtos.ReceitaInsumoDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.InsumoRepository;
import com.spea.api.repositories.ReceitaInsumoRepository;
import com.spea.api.repositories.ReceitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceitaInsumoServiceTest {

    @InjectMocks
    private ReceitaInsumoService receitaInsumoService;

    @Mock
    private ReceitaInsumoRepository receitaInsumoRepository;

    @Mock
    private InsumoRepository insumoRepository;

    @Mock
    private ReceitaRepository receitaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Deve criar associação entre receita e insumo com sucesso quando os dados forem válidos")
    void deveCriarAssociacaoComSucesso() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Margherita");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("50.00"));

        InsumoDto insumoEncontradoDto = new InsumoDto();
        insumoEncontradoDto.setQuantidadePorPacote(500.0);
        insumoEncontradoDto.setValorPagoPorPacote(new BigDecimal("25.00"));

        ReceitaInsumoDto associacaoSalva = new ReceitaInsumoDto();
        associacaoSalva.setReceitaId(receitaId);
        associacaoSalva.setInsumoId(insumoId);
        associacaoSalva.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
        associacaoSalva.setValorGastoInsumo(new BigDecimal("5.02"));

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoEncontradoDto);
        when(receitaInsumoRepository.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, new BigDecimal("5.02")))
                .thenReturn(associacaoSalva);

        // Act
        ReceitaInsumoDto resultado = receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo);

        // Assert
        assertNotNull(resultado);
        assertEquals(receitaId, resultado.getReceitaId());
        assertEquals(insumoId, resultado.getInsumoId());
        assertEquals(quantidadeUtilizadaInsumo, resultado.getQuantidadeUtilizadaInsumo());
        assertEquals(new BigDecimal("5.02"), resultado.getValorGastoInsumo());

        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository).obterReceitaPeloId(receitaId);
        verify(insumoRepository).obterInsumoPeloId(insumoId);
        verify(receitaInsumoRepository).criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, new BigDecimal("5.02"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade utilizada de insumo for nula")
    void deveLancarExcecaoQuandoQuantidadeUtilizadaInsumoForNula() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = null;

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("A quantidade utilizada de insumo é obrigatória.", excecao.getMessage());

        verify(receitaRepository, never()).verificarExistenciaDaReceitaPeloId(any());
        verify(insumoRepository, never()).verificarExistenciaDoInsumoPeloId(any());
        verify(receitaRepository, never()).obterReceitaPeloId(any());
        verify(insumoRepository, never()).obterInsumoPeloId(any());
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade utilizada de insumo for menor ou igual a zero")
    void deveLancarExcecaoQuandoQuantidadeUtilizadaInsumoForMenorOuIgualAZero() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;

        // Testando valor zero
        BigDecimal quantidadeUtilizadaInsumoZero = BigDecimal.ZERO;

        // Testando valor negativo
        BigDecimal quantidadeUtilizadaInsumoNegativo = new BigDecimal("-10.50");

        // Act & Assert - Teste com valor ZERO
        EmpreendedorErrorException excecaoZero = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumoZero));

        assertEquals("A quantidade utilizada de insumo deve ser maior que 0.", excecaoZero.getMessage());

        // Act & Assert - Teste com valor NEGATIVO
        EmpreendedorErrorException excecaoNegativo = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumoNegativo));

        assertEquals("A quantidade utilizada de insumo deve ser maior que 0.", excecaoNegativo.getMessage());

        // Verificações comuns para ambos os casos
        verify(receitaRepository, never()).verificarExistenciaDaReceitaPeloId(any());
        verify(insumoRepository, never()).verificarExistenciaDoInsumoPeloId(any());
        verify(receitaRepository, never()).obterReceitaPeloId(any());
        verify(insumoRepository, never()).obterInsumoPeloId(any());
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a receita não for encontrada pelo ID")
    void deveLancarExcecaoQuandoReceitaNaoForEncontrada() {
        // Arrange
        Long receitaId = 999L; // ID que não existe
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(false);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("Nenhuma receita encontrada pelo id informado.", excecao.getMessage());

        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository, never()).verificarExistenciaDoInsumoPeloId(any());
        verify(receitaRepository, never()).obterReceitaPeloId(any());
        verify(insumoRepository, never()).obterInsumoPeloId(any());
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o insumo não for encontrado pelo ID")
    void deveLancarExcecaoQuandoInsumoNaoForEncontrado() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 999L; // ID que não existe
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(false);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("Nenhum insumo encontrado pelo id informado.", excecao.getMessage());

        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository, never()).obterReceitaPeloId(any());
        verify(insumoRepository, never()).obterInsumoPeloId(any());
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade por pacote do insumo for nula")
    void deveLancarExcecaoQuandoQuantidadePorPacoteForNula() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Margherita");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("50.00"));

        InsumoDto insumoEncontradoDto = new InsumoDto();
        insumoEncontradoDto.setQuantidadePorPacote(null); // QUANTIDADE POR PACOTE NULA
        insumoEncontradoDto.setValorPagoPorPacote(new BigDecimal("25.00"));

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoEncontradoDto);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("A quantidade por pacote não pode ser nula.", excecao.getMessage());

        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository).obterReceitaPeloId(receitaId);
        verify(insumoRepository).obterInsumoPeloId(insumoId);
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade por pacote do insumo for menor ou igual a zero")
    void deveLancarExcecaoQuandoQuantidadePorPacoteForMenorOuIgualAZero() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Margherita");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("50.00"));

        // Testando valor ZERO
        InsumoDto insumoComQuantidadeZero = new InsumoDto();
        insumoComQuantidadeZero.setQuantidadePorPacote(0.0);
        insumoComQuantidadeZero.setValorPagoPorPacote(new BigDecimal("25.00"));

        // Testando valor NEGATIVO
        InsumoDto insumoComQuantidadeNegativa = new InsumoDto();
        insumoComQuantidadeNegativa.setQuantidadePorPacote(-500.0);
        insumoComQuantidadeNegativa.setValorPagoPorPacote(new BigDecimal("25.00"));

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);

        // Act & Assert - Teste com valor ZERO
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoComQuantidadeZero);

        EmpreendedorErrorException excecaoZero = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("A quantidade por pacote deve ser maior que 0.", excecaoZero.getMessage());

        // Act & Assert - Teste com valor NEGATIVO
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoComQuantidadeNegativa);

        EmpreendedorErrorException excecaoNegativo = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("A quantidade por pacote deve ser maior que 0.", excecaoNegativo.getMessage());

        // Verificações comuns para ambos os casos
        verify(receitaRepository, times(2)).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository, times(2)).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository, times(2)).obterReceitaPeloId(receitaId);
        verify(insumoRepository, times(2)).obterInsumoPeloId(insumoId);
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o valor pago por pacote do insumo for nulo")
    void deveLancarExcecaoQuandoValorPagoPorPacoteForNulo() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Margherita");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("50.00"));

        InsumoDto insumoEncontradoDto = new InsumoDto();
        insumoEncontradoDto.setQuantidadePorPacote(500.0);
        insumoEncontradoDto.setValorPagoPorPacote(null); // VALOR PAGO POR PACOTE NULO

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoEncontradoDto);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("O valor pago por pacote não pode ser nulo.", excecao.getMessage());

        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository).obterReceitaPeloId(receitaId);
        verify(insumoRepository).obterInsumoPeloId(insumoId);
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o valor pago por pacote do insumo for menor ou igual a zero")
    void deveLancarExcecaoQuandoValorPagoPorPacoteForMenorOuIgualAZero() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Margherita");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("50.00"));

        // Testando valor ZERO
        InsumoDto insumoComValorZero = new InsumoDto();
        insumoComValorZero.setQuantidadePorPacote(500.0);
        insumoComValorZero.setValorPagoPorPacote(BigDecimal.ZERO);

        // Testando valor NEGATIVO
        InsumoDto insumoComValorNegativo = new InsumoDto();
        insumoComValorNegativo.setQuantidadePorPacote(500.0);
        insumoComValorNegativo.setValorPagoPorPacote(new BigDecimal("-25.00"));

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);

        // Act & Assert - Teste com valor ZERO
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoComValorZero);

        EmpreendedorErrorException excecaoZero = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("O valor pago por pacote deve ser maior que 0.", excecaoZero.getMessage());

        // Act & Assert - Teste com valor NEGATIVO
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoComValorNegativo);

        EmpreendedorErrorException excecaoNegativo = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("O valor pago por pacote deve ser maior que 0.", excecaoNegativo.getMessage());

        // Verificações comuns para ambos os casos
        verify(receitaRepository, times(2)).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository, times(2)).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository, times(2)).obterReceitaPeloId(receitaId);
        verify(insumoRepository, times(2)).obterInsumoPeloId(insumoId);
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve usar arredondamento HALF_EVEN corretamente quando o valor estiver no meio exato")
    void deveUsarArredondamentoHalfEvenCorretamente() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50"); // Valor que causará arredondamento

        // Configuração para gerar um cálculo que resulte em 5.025 (exatamente no meio)
        // 25.00 / 500.0 = 0.05 por unidade
        // 0.05 * 100.50 = 5.025 → deve arredondar para 5.02 (número par mais próximo)
        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Margherita");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("50.00"));

        InsumoDto insumoEncontradoDto = new InsumoDto();
        insumoEncontradoDto.setQuantidadePorPacote(500.0);
        insumoEncontradoDto.setValorPagoPorPacote(new BigDecimal("25.00")); // 25.00 / 500 = 0.05

        ReceitaInsumoDto associacaoSalva = new ReceitaInsumoDto();
        associacaoSalva.setReceitaId(receitaId);
        associacaoSalva.setInsumoId(insumoId);
        associacaoSalva.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
        associacaoSalva.setValorGastoInsumo(new BigDecimal("5.02")); // HALF_EVEN: 5.025 → 5.02

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoEncontradoDto);
        when(receitaInsumoRepository.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, new BigDecimal("5.02")))
                .thenReturn(associacaoSalva);

        // Act
        ReceitaInsumoDto resultado = receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo);

        // Assert - Foco no arredondamento correto
        assertEquals(new BigDecimal("5.02"), resultado.getValorGastoInsumo());
        assertEquals(0, new BigDecimal("5.02").compareTo(resultado.getValorGastoInsumo()));

        // Verificações adicionais
        verify(receitaRepository).verificarExistenciaDaReceitaPeloId(receitaId);
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(insumoId);
        verify(receitaRepository).obterReceitaPeloId(receitaId);
        verify(insumoRepository).obterInsumoPeloId(insumoId);
        verify(receitaInsumoRepository).criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, new BigDecimal("5.02"));
    }

    @Test
    @DisplayName("Deve usar arredondamento HALF_EVEN para cima quando o valor estiver no meio e o anterior for ímpar")
    void deveUsarArredondamentoHalfEvenParaCima() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 3L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.75");

        // Configuração para gerar um cálculo que resulte em 5.0375
        // 20.00 / 400.0 = 0.05 por unidade
        // 0.05 * 100.75 = 5.0375 → deve arredondar para 5.04 (próximo par)
        ReceitaDto receitaEncontradaDto = new ReceitaDto();
        receitaEncontradaDto.setNome("Pizza Calabresa");
        receitaEncontradaDto.setTotalGastoInsumos(new BigDecimal("30.00"));

        InsumoDto insumoEncontradoDto = new InsumoDto();
        insumoEncontradoDto.setQuantidadePorPacote(400.0);
        insumoEncontradoDto.setValorPagoPorPacote(new BigDecimal("20.00")); // 20.00 / 400 = 0.05

        ReceitaInsumoDto associacaoSalva = new ReceitaInsumoDto();
        associacaoSalva.setReceitaId(receitaId);
        associacaoSalva.setInsumoId(insumoId);
        associacaoSalva.setQuantidadeUtilizadaInsumo(quantidadeUtilizadaInsumo);
        associacaoSalva.setValorGastoInsumo(new BigDecimal("5.04")); // HALF_EVEN: 5.0375 → 5.04

        when(receitaRepository.verificarExistenciaDaReceitaPeloId(receitaId)).thenReturn(true);
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(insumoId)).thenReturn(true);
        when(receitaRepository.obterReceitaPeloId(receitaId)).thenReturn(receitaEncontradaDto);
        when(insumoRepository.obterInsumoPeloId(insumoId)).thenReturn(insumoEncontradoDto);
        when(receitaInsumoRepository.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, new BigDecimal("5.04")))
                .thenReturn(associacaoSalva);

        // Act
        ReceitaInsumoDto resultado = receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo);

        // Assert
        assertEquals(new BigDecimal("5.04"), resultado.getValorGastoInsumo());

        verify(receitaInsumoRepository).criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo, new BigDecimal("5.04"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando já existir associação entre a receita e o insumo")
    void deveLancarExcecaoQuandoJaExistirAssociacao() {
        // Arrange
        Long receitaId = 1L;
        Long insumoId = 2L;
        BigDecimal quantidadeUtilizadaInsumo = new BigDecimal("100.50");

        when(receitaInsumoRepository.verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId)).thenReturn(true);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class,
                () -> receitaInsumoService.criarAssociacao(receitaId, insumoId, quantidadeUtilizadaInsumo));

        assertEquals("O insumo informado já está associado à receita informada.", excecao.getMessage());

        verify(receitaInsumoRepository).verificarExistenciaDaAssociacaoDaReceitaEInsumo(receitaId, insumoId);
        verify(receitaRepository, never()).verificarExistenciaDaReceitaPeloId(any());
        verify(insumoRepository, never()).verificarExistenciaDoInsumoPeloId(any());
        verify(receitaRepository, never()).obterReceitaPeloId(any());
        verify(insumoRepository, never()).obterInsumoPeloId(any());
        verify(receitaInsumoRepository, never()).criarAssociacao(any(), any(), any(), any());
    }
}