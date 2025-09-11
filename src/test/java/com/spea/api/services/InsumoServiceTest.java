package com.spea.api.services;

import com.spea.api.dtos.AssociacaoDto;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsumoServiceTest {

    @InjectMocks
    private InsumoService insumoService;

    @Mock
    private InsumoRepository insumoRepository;

    @Mock
    private ReceitaInsumoRepository receitaInsumoRepository;

    @Mock
    private ReceitaRepository receitaRepository;

    @Mock
    private ReceitaService receitaService;

    @Mock
    private ReceitaInsumoService receitaInsumoService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Método cadastrarInsumo
    @Test
    @DisplayName("Deve cadastrar com sucesso um novo insumo.")
    void deveCadastrarComSucessoUmNovoInsumo() {
        String nome = "Muçarela";
        Double quantidadePorPacote = 500.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("16.90");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        insumoService.cadastrarInsumo(dto);

        verify(insumoRepository).cadastrarInsumo(dto);
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando o nome for nulo.")
    void deveOcorrerUmaExcecaoQuandoONomeForNulo() {
        String nome = null;
        Double quantidadePorPacote = 1000.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("33.90");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
           insumoService.cadastrarInsumo(dto);
        });

        assertEquals("O nome do insumo é obrigatório.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando o nome for composto apenas por espaços em branco.")
    void deveOcorrerUmaExcecaoQuandoONomeForCompostoApenasPorEspacosEmBranco() {
        String nome = "           ";
        Double quantidadePorPacote = 1000.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("33.90");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.cadastrarInsumo(dto);
        });

        assertEquals("O nome do insumo é obrigatório.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando o nome for maior que 100 caracteres.")
    void deveOcorrerUmExcecaoQuandoONomeForMaiorQue100Caracteres() {
        String nome = "Extrato seco de raiz de ginseng siberiano (Eleutherococcus senticosus) padronizado em 0,8% eleuterosídeos e 0,2% de compostos polifenólicos";
        Double quantidadePorPacote = 500.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("1006.90");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.cadastrarInsumo(dto);
        });

        assertEquals("O nome do insumo deve ter no máximo 100 caracteres.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando a quantidade por pacote for nula.")
    void deveOcorrerUmaExcecaoQuandoAQuantidadePorPacoteForNula() {
        String nome = "Extrato de tomate";
        Double quantidadePorPacote = null;
        BigDecimal valorPagoPorPacote = new BigDecimal("4.90");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.cadastrarInsumo(dto);
        });

        assertEquals("A quantidade do insumo por pacote é obrigatória.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando a quantidade por pacote for menor ou igual a zero.")
    void deveOcorrerUmaExcecaoQuandoAQuantidadePorPacoteForMenorOuIgualAZero() {
        String nome = "Extrato de tomate";
        Double quantidadePorPacote = -1.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("4.90");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.cadastrarInsumo(dto);
        });

        assertEquals("A quantidade de insumo por pacote deve ser maior que 0.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando o valor pago por pacote for nulo.")
    void deveOcorrerUmaExcecaoQuandoOValorPagoPorPacoteForNulo() {
        String nome = "Batata Inglesa";
        Double quantidadePorPacote = 1000.00;
        BigDecimal valorPagoPorPacote = null;

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.cadastrarInsumo(dto);
        });

        assertEquals("O valor pago por cada pacote de insumo é obrigatório.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    @Test
    @DisplayName("Deve ocorrer uma exceção quando o valor pago por pacote for menor ou igual a zero.")
    void deveOcorrerUmaExcecaoQuandoOValorPagoPorPacoteForMenorOuIgualAZero() {
        String nome = "Batata Inglesa";
        Double quantidadePorPacote = 1000.00;
        BigDecimal valorPagoPorPacote = new BigDecimal("0.00");

        InsumoDto dto = new InsumoDto();
        dto.setNome(nome);
        dto.setQuantidadePorPacote(quantidadePorPacote);
        dto.setValorPagoPorPacote(valorPagoPorPacote);

        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.cadastrarInsumo(dto);
        });

        assertEquals("O valor pago por cada pacote de insumo deve ser maior que 0.", excecao.getMessage());

        verify(insumoRepository, never()).cadastrarInsumo(any());
    }

    // Método atualizarInsumo
    @Test
    @DisplayName("Deve atualizar um insumo com sucesso.")
    void deveAtualizarUmInsumoComSucesso() {
        // Dados de entrada
        Long id = 1L;
        InsumoDto dto = new InsumoDto();
        dto.setNome("Farinha de Trigo Integral");
        dto.setQuantidadePorPacote(500.00);
        dto.setValorPagoPorPacote(new BigDecimal("7.50"));

        // Configuração do mock
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(insumoRepository.atualizarInsumo(id, dto)).thenReturn(dto);

        // Execução
        InsumoDto resultado = insumoService.atualizarInsumo(id, dto);

        // Verificações
        assertNotNull(resultado);
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(id);
        verify(insumoRepository).atualizarInsumo(id, dto);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome for inválido durante atualização.")
    void deveLancarExcecaoQuandoNomeForInvalidoDuranteAtualizacao() {
        // Dados de entrada
        Long id = 1L;
        InsumoDto dto = new InsumoDto();
        dto.setNome(null); // Nome inválido
        dto.setQuantidadePorPacote(1000.00);
        dto.setValorPagoPorPacote(new BigDecimal("5.90"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.atualizarInsumo(id, dto);
        });

        assertEquals("O nome do insumo é obrigatório.", excecao.getMessage());
        verify(insumoRepository, never()).atualizarInsumo(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a quantidade por pacote for inválida durante atualização.")
    void deveLancarExcecaoQuandoQuantidadePorPacoteForInvalidaDuranteAtualizacao() {
        // Dados de entrada
        Long id = 1L;
        InsumoDto dto = new InsumoDto();
        dto.setNome("Farinha de Trigo");
        dto.setQuantidadePorPacote(-1.00); // Quantidade inválida
        dto.setValorPagoPorPacote(new BigDecimal("5.90"));

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.atualizarInsumo(id, dto);
        });

        assertEquals("A quantidade de insumo por pacote deve ser maior que 0.", excecao.getMessage());
        verify(insumoRepository, never()).atualizarInsumo(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o valor pago por pacote for inválido durante atualização.")
    void deveLancarExcecaoQuandoValorPagoPorPacoteForInvalidoDuranteAtualizacao() {
        // Dados de entrada
        Long id = 1L;
        InsumoDto dto = new InsumoDto();
        dto.setNome("Farinha de Trigo");
        dto.setQuantidadePorPacote(1000.00);
        dto.setValorPagoPorPacote(new BigDecimal("0.00")); // Valor inválido

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.atualizarInsumo(id, dto);
        });

        assertEquals("O valor pago por cada pacote de insumo deve ser maior que 0.", excecao.getMessage());
        verify(insumoRepository, never()).atualizarInsumo(any(), any());
    }

    @Test
    @DisplayName("Deve atualizar insumo sem associações com sucesso")
    void deveAtualizarInsumoSemAssociacoesComSucesso() {
        // Arrange
        Long id = 1L;
        InsumoDto insumoDto = new InsumoDto();
        insumoDto.setNome("Farinha Atualizada");
        insumoDto.setQuantidadePorPacote(1000.0);
        insumoDto.setValorPagoPorPacote(new BigDecimal("12.50"));

        InsumoDto insumoAtualizado = new InsumoDto();
        insumoAtualizado.setId(id);
        insumoAtualizado.setNome("Farinha Atualizada");
        insumoAtualizado.setQuantidadePorPacote(1000.0);
        insumoAtualizado.setValorPagoPorPacote(new BigDecimal("12.50"));

        List<AssociacaoDto> listaVazia = new ArrayList<>();

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id)).thenReturn(listaVazia);
        when(insumoRepository.atualizarInsumo(id, insumoDto)).thenReturn(insumoAtualizado);

        // Act
        InsumoDto resultado = insumoService.atualizarInsumo(id, insumoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Farinha Atualizada", resultado.getNome());
        assertEquals(1000.0, resultado.getQuantidadePorPacote());
        assertEquals(new BigDecimal("12.50"), resultado.getValorPagoPorPacote());

        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(id);
        verify(receitaInsumoRepository).obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id);
        verify(insumoRepository).atualizarInsumo(id, insumoDto);
        verify(receitaInsumoRepository, never()).atualizarReceitaInsumo(any(), any(), any(), any());
        verify(receitaRepository, never()).atualizarTotalGastoInsumosDaReceita(any(), any());
    }

    @Test
    @DisplayName("Deve atualizar insumo com associações e recalcular custos das receitas")
    void deveAtualizarInsumoComAssociacoesERecalcularCustos() {
        // Arrange
        Long id = 1L;
        InsumoDto insumoDto = new InsumoDto();
        insumoDto.setNome("Farinha Atualizada");
        insumoDto.setQuantidadePorPacote(1000.0);
        insumoDto.setValorPagoPorPacote(new BigDecimal("10.00"));

        InsumoDto insumoAtualizado = new InsumoDto();
        insumoAtualizado.setId(id);
        insumoAtualizado.setNome("Farinha Atualizada");
        insumoAtualizado.setQuantidadePorPacote(1000.0);
        insumoAtualizado.setValorPagoPorPacote(new BigDecimal("10.00"));

        // Mock das associações com ReceitaDto incluído
        ReceitaInsumoDto associacao1 = criarAssociacao(1L, id, new BigDecimal("200.00"), new BigDecimal("2.00"));
        ReceitaInsumoDto associacao2 = criarAssociacao(2L, id, new BigDecimal("300.00"), new BigDecimal("3.00"));

        ReceitaDto receita1 = new ReceitaDto();
        receita1.setId(1L);
        receita1.setTotalGastoInsumos(new BigDecimal("50.00"));

        ReceitaDto receita2 = new ReceitaDto();
        receita2.setId(2L);
        receita2.setTotalGastoInsumos(new BigDecimal("75.00"));

        // Criar AssociacaoDto com ambos os DTOs
        AssociacaoDto associacaoCompleta1 = new AssociacaoDto();
        associacaoCompleta1.setReceitaInsumoDto(associacao1);
        associacaoCompleta1.setReceitaDto(receita1);

        AssociacaoDto associacaoCompleta2 = new AssociacaoDto();
        associacaoCompleta2.setReceitaInsumoDto(associacao2);
        associacaoCompleta2.setReceitaDto(receita2);

        List<AssociacaoDto> associacoesCompletas = Arrays.asList(associacaoCompleta1, associacaoCompleta2);

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(associacoesCompletas);
        when(receitaInsumoService.calcularGastoComInsumo(1000.0, new BigDecimal("10.00"), new BigDecimal("200.00")))
                .thenReturn(new BigDecimal("2.00"));
        when(receitaInsumoService.calcularGastoComInsumo(1000.0, new BigDecimal("10.00"), new BigDecimal("300.00")))
                .thenReturn(new BigDecimal("3.00"));
        when(insumoRepository.atualizarInsumo(id, insumoDto)).thenReturn(insumoAtualizado);

        // Act
        InsumoDto resultado = insumoService.atualizarInsumo(id, insumoDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());

        // Verifica que os custos foram atualizados
        verify(receitaInsumoRepository).atualizarReceitaInsumo(1L, id, new BigDecimal("200.00"), new BigDecimal("2.00"));
        verify(receitaInsumoRepository).atualizarReceitaInsumo(2L, id, new BigDecimal("300.00"), new BigDecimal("3.00"));

        // Verifica que os totais das receitas foram atualizados
        ArgumentCaptor<ReceitaDto> receitaDtoCaptor = ArgumentCaptor.forClass(ReceitaDto.class);
        verify(receitaRepository).atualizarTotalGastoInsumosDaReceita(eq(1L), receitaDtoCaptor.capture());
        assertEquals(0, new BigDecimal("50.00").compareTo(receitaDtoCaptor.getValue().getTotalGastoInsumos()));

        verify(receitaRepository).atualizarTotalGastoInsumosDaReceita(eq(2L), receitaDtoCaptor.capture());
        assertEquals(0, new BigDecimal("75.00").compareTo(receitaDtoCaptor.getValue().getTotalGastoInsumos()));
    }

    @Test
    @DisplayName("Deve garantir que total não fique negativo durante atualização")
    void deveGarantirQueTotalNaoFiqueNegativoDuranteAtualizacao() {
        // Arrange
        Long id = 1L;
        InsumoDto insumoDto = new InsumoDto();
        insumoDto.setNome("Farinha");
        insumoDto.setQuantidadePorPacote(1000.0);
        insumoDto.setValorPagoPorPacote(new BigDecimal("20.00"));

        // Associação com valor antigo maior que o total atual
        ReceitaInsumoDto associacao = criarAssociacao(1L, id, new BigDecimal("100.00"), new BigDecimal("15.00"));
        ReceitaDto receita = new ReceitaDto();
        receita.setId(1L);
        receita.setTotalGastoInsumos(new BigDecimal("10.00"));

        // Criar AssociacaoDto com ambos os DTOs
        AssociacaoDto associacaoCompleta = new AssociacaoDto();
        associacaoCompleta.setReceitaInsumoDto(associacao);
        associacaoCompleta.setReceitaDto(receita);

        List<AssociacaoDto> associacoesCompletas = List.of(associacaoCompleta);

        InsumoDto insumoAtualizado = new InsumoDto();
        insumoAtualizado.setId(id);
        insumoAtualizado.setNome("Farinha");
        insumoAtualizado.setQuantidadePorPacote(1000.0);
        insumoAtualizado.setValorPagoPorPacote(new BigDecimal("20.00"));

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(associacoesCompletas);
        when(receitaInsumoService.calcularGastoComInsumo(1000.0, new BigDecimal("20.00"), new BigDecimal("100.00")))
                .thenReturn(new BigDecimal("2.00"));
        when(insumoRepository.atualizarInsumo(id, insumoDto)).thenReturn(insumoAtualizado);

        // Act
        InsumoDto resultado = insumoService.atualizarInsumo(id, insumoDto);

        // Assert - Verifica que o total não ficou negativo
        ArgumentCaptor<ReceitaDto> receitaDtoCaptor = ArgumentCaptor.forClass(ReceitaDto.class);
        verify(receitaRepository).atualizarTotalGastoInsumosDaReceita(eq(1L), receitaDtoCaptor.capture());

        // Cálculo: 10.00 - 15.00 (antigo) = -5.00 → max(0) = 0.00 + 2.00 (novo) = 2.00
        assertEquals(0, new BigDecimal("2.00").compareTo(receitaDtoCaptor.getValue().getTotalGastoInsumos()));

        // Verifica que a atualização da associação foi chamada
        verify(receitaInsumoRepository).atualizarReceitaInsumo(1L, id, new BigDecimal("100.00"), new BigDecimal("2.00"));
    }

    // Método deletarInsumo
    @Test
    @DisplayName("Deve deletar insumo sem associações com sucesso")
    void deveDeletarInsumoSemAssociacoesComSucesso() {
        // Arrange
        Long id = 1L;
        List<AssociacaoDto> listaVazia = new ArrayList<>();

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(listaVazia);
        doNothing().when(insumoRepository).deletarInsumo(id);

        // Act
        insumoService.deletarInsumo(id);

        // Assert
        verify(insumoRepository).verificarExistenciaDoInsumoPeloId(id);
        verify(receitaInsumoRepository).obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id);
        verify(insumoRepository).deletarInsumo(id);
        verify(receitaRepository, never()).obterReceitaPeloId(any());
        verify(receitaRepository, never()).atualizarTotalGastoInsumosDaReceita(any(), any());
    }

    @Test
    @DisplayName("Deve deletar insumo com associações e atualizar totais das receitas")
    void deveDeletarInsumoComAssociacionesEAtualizarTotais() {
        // Arrange
        Long id = 1L;

        // Mock das associações do insumo - AGORA usando AssociacaoDto
        ReceitaInsumoDto associacao1 = criarAssociacao(1L, id, new BigDecimal("100.50"), new BigDecimal("25.25"));
        ReceitaInsumoDto associacao2 = criarAssociacao(2L, id, new BigDecimal("75.25"), new BigDecimal("18.75"));

        // Mock das receitas associadas
        ReceitaDto receita1 = new ReceitaDto();
        receita1.setId(1L);
        receita1.setTotalGastoInsumos(new BigDecimal("150.00"));

        ReceitaDto receita2 = new ReceitaDto();
        receita2.setId(2L);
        receita2.setTotalGastoInsumos(new BigDecimal("200.00"));

        // Criar AssociacaoDto com ambos os DTOs
        AssociacaoDto associacaoCompleta1 = new AssociacaoDto();
        associacaoCompleta1.setReceitaInsumoDto(associacao1);
        associacaoCompleta1.setReceitaDto(receita1);

        AssociacaoDto associacaoCompleta2 = new AssociacaoDto();
        associacaoCompleta2.setReceitaInsumoDto(associacao2);
        associacaoCompleta2.setReceitaDto(receita2);

        List<AssociacaoDto> associacoesCompletas = Arrays.asList(associacaoCompleta1, associacaoCompleta2);

        ReceitaDto receitaAtualizada1 = new ReceitaDto();
        receitaAtualizada1.setId(1L);
        receitaAtualizada1.setTotalGastoInsumos(new BigDecimal("124.75"));

        ReceitaDto receitaAtualizada2 = new ReceitaDto();
        receitaAtualizada2.setId(2L);
        receitaAtualizada2.setTotalGastoInsumos(new BigDecimal("181.25"));

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(associacoesCompletas);
        when(receitaRepository.atualizarTotalGastoInsumosDaReceita(eq(1L), any(ReceitaDto.class))).thenReturn(receitaAtualizada1);
        when(receitaRepository.atualizarTotalGastoInsumosDaReceita(eq(2L), any(ReceitaDto.class))).thenReturn(receitaAtualizada2);
        doNothing().when(insumoRepository).deletarInsumo(id);

        // Act
        insumoService.deletarInsumo(id);

        // Assert - Verifica que os totais foram atualizados corretamente
        verify(receitaRepository).atualizarTotalGastoInsumosDaReceita(eq(1L), argThat(dto ->
                dto.getTotalGastoInsumos().equals(new BigDecimal("124.75")))); // 150 - 25.25
        verify(receitaRepository).atualizarTotalGastoInsumosDaReceita(eq(2L), argThat(dto ->
                dto.getTotalGastoInsumos().equals(new BigDecimal("181.25")))); // 200 - 18.75
        verify(insumoRepository).deletarInsumo(id);
    }

    @Test
    @DisplayName("Deve garantir que total não fique negativo após subtração")
    void deveGarantirQueTotalNaoFiqueNegativo() {
        // Arrange
        Long id = 1L;

        // Criar associação usando AssociacaoDto
        ReceitaInsumoDto associacao = criarAssociacao(1L, id, new BigDecimal("50.00"), new BigDecimal("30.00"));
        ReceitaDto receita = new ReceitaDto();
        receita.setId(1L);
        receita.setTotalGastoInsumos(new BigDecimal("25.00")); // Total menor que o valor a subtrair

        // Criar AssociacaoDto com ambos os DTOs
        AssociacaoDto associacaoCompleta = new AssociacaoDto();
        associacaoCompleta.setReceitaInsumoDto(associacao);
        associacaoCompleta.setReceitaDto(receita);

        List<AssociacaoDto> associacoesCompletas = List.of(associacaoCompleta);

        ReceitaDto receitaAtualizada = new ReceitaDto();
        receitaAtualizada.setId(1L);
        receitaAtualizada.setTotalGastoInsumos(new BigDecimal("0.00"));

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(associacoesCompletas);
        when(receitaRepository.atualizarTotalGastoInsumosDaReceita(eq(1L), any(ReceitaDto.class))).thenReturn(receitaAtualizada);
        doNothing().when(insumoRepository).deletarInsumo(id);

        // Act
        insumoService.deletarInsumo(id);

        // Assert - Verifica que o método foi chamado e captura o argumento
        ArgumentCaptor<ReceitaDto> receitaDtoCaptor = ArgumentCaptor.forClass(ReceitaDto.class);
        verify(receitaRepository).atualizarTotalGastoInsumosDaReceita(eq(1L), receitaDtoCaptor.capture());

        ReceitaDto dtoAtualizado = receitaDtoCaptor.getValue();
        assertEquals(0, new BigDecimal("0.00").compareTo(dtoAtualizado.getTotalGastoInsumos()));

        verify(insumoRepository).deletarInsumo(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando total atual for negativo")
    void deveLancarExcecaoQuandoTotalAtualForNegativo() {
        // Arrange
        Long id = 1L;

        // Criar associação usando AssociacaoDto
        ReceitaInsumoDto associacao = criarAssociacao(1L, id, new BigDecimal("50.00"), new BigDecimal("10.00"));
        ReceitaDto receita = new ReceitaDto();
        receita.setId(1L);
        receita.setTotalGastoInsumos(new BigDecimal("-5.00")); // Total negativo

        // Criar AssociacaoDto com ambos os DTOs
        AssociacaoDto associacaoCompleta = new AssociacaoDto();
        associacaoCompleta.setReceitaInsumoDto(associacao);
        associacaoCompleta.setReceitaDto(receita);

        List<AssociacaoDto> associacoesCompletas = List.of(associacaoCompleta);

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(associacoesCompletas);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.deletarInsumo(id);
        });

        assertEquals("O total atual não pode ser negativo.", excecao.getMessage());
        verify(insumoRepository, never()).deletarInsumo(any());

        // Verifica que não houve tentativa de atualizar a receita
        verify(receitaRepository, never()).atualizarTotalGastoInsumosDaReceita(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor a subtrair for negativo")
    void deveLancarExcecaoQuandoValorASubtrairForNegativo() {
        // Arrange
        Long id = 1L;

        // Criar associação usando AssociacaoDto
        ReceitaInsumoDto associacao = criarAssociacao(1L, id, new BigDecimal("50.00"), new BigDecimal("-5.00")); // Valor negativo
        ReceitaDto receita = new ReceitaDto();
        receita.setId(1L);
        receita.setTotalGastoInsumos(new BigDecimal("100.00"));

        // Criar AssociacaoDto com ambos os DTOs
        AssociacaoDto associacaoCompleta = new AssociacaoDto();
        associacaoCompleta.setReceitaInsumoDto(associacao);
        associacaoCompleta.setReceitaDto(receita);

        List<AssociacaoDto> associacoesCompletas = List.of(associacaoCompleta);

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(true);
        when(receitaInsumoRepository.obterListaDeAssociacoesEReceitasRelacionadasAoMesmoInsumo(id))
                .thenReturn(associacoesCompletas);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.deletarInsumo(id);
        });

        assertEquals("O valor gasto não pode ser negativo.", excecao.getMessage());
        verify(insumoRepository, never()).deletarInsumo(any());

        // Verifica que não houve tentativa de atualizar a receita
        verify(receitaRepository, never()).atualizarTotalGastoInsumosDaReceita(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do insumo não existir")
    void deveLancarExcecaoQuandoIdInsumoNaoExistir() {
        // Arrange
        Long id = 999L;

        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(false);

        // Act & Assert
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.deletarInsumo(id);
        });

        assertEquals("O ID do insumo informado não está cadastrado.", excecao.getMessage());
        verify(insumoRepository, never()).deletarInsumo(any());
        verify(receitaInsumoRepository, never()).obterListaDeInsumosAssociadosAReceitasPeloId(any());
    }

    // Método auxiliar para criar associações
    private ReceitaInsumoDto criarAssociacao(Long receitaId, Long insumoId, BigDecimal quantidade, BigDecimal valor) {
        ReceitaInsumoDto dto = new ReceitaInsumoDto();
        dto.setReceitaId(receitaId);
        dto.setInsumoId(insumoId);
        dto.setQuantidadeUtilizadaInsumo(quantidade);
        dto.setValorGastoInsumo(valor);
        return dto;
    }


}