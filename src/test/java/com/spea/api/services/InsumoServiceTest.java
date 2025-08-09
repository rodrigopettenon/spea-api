package com.spea.api.services;

import com.spea.api.dtos.InsumoDto;
import com.spea.api.exceptions.EmpreendedorErrorException;
import com.spea.api.repositories.InsumoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    // Método obterListaDeInsumos
    @Test
    @DisplayName("Deve obter uma lista de insumos com sucesso.")
    void deveObterUmaListaDeInsumosComSucesso() {
        // Configuração do mock
        List<InsumoDto> listaMock = new ArrayList<>();

        InsumoDto primeiroInsumoDto = new InsumoDto();
        primeiroInsumoDto.setId(1L);
        primeiroInsumoDto.setNome("Farinha de Trigo");
        primeiroInsumoDto.setQuantidadePorPacote(1000.00);
        primeiroInsumoDto.setValorPagoPorPacote(new BigDecimal("5.90"));

        InsumoDto segundoInsumoDto = new InsumoDto();
        segundoInsumoDto.setId(2L);
        segundoInsumoDto.setNome("Açúcar");
        segundoInsumoDto.setQuantidadePorPacote(1000.00);
        segundoInsumoDto.setValorPagoPorPacote(new BigDecimal("4.50"));

        listaMock.add(primeiroInsumoDto);
        listaMock.add(segundoInsumoDto);

        when(insumoRepository.obterListaDeInsumos()).thenReturn(listaMock);

        // Execução
        List<InsumoDto> resultado = insumoService.obterListaDeInsumos();

        // Verificações
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(insumoRepository).obterListaDeInsumos();
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
    @DisplayName("Deve lançar exceção quando o insumo não existir pelo ID informado.")
    void deveLancarExcecaoQuandoInsumoNaoExistirPeloId() {
        // Dados de entrada
        Long id = 999L;
        InsumoDto dto = new InsumoDto();
        dto.setNome("Farinha de Trigo");
        dto.setQuantidadePorPacote(1000.00);
        dto.setValorPagoPorPacote(new BigDecimal("5.90"));

        // Configuração do mock
        when(insumoRepository.verificarExistenciaDoInsumoPeloId(id)).thenReturn(false);

        // Execução e verificação
        EmpreendedorErrorException excecao = assertThrows(EmpreendedorErrorException.class, () -> {
            insumoService.atualizarInsumo(id, dto);
        });

        assertEquals("O ID do insumo informado não está cadastrado.", excecao.getMessage());
        verify(insumoRepository, never()).atualizarInsumo(any(), any());
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
}