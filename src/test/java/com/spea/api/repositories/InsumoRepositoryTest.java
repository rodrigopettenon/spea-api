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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
}