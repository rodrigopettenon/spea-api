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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
}