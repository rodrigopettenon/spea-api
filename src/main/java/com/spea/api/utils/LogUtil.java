package com.spea.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);



    // LOGS DE INICIOS

    //Genérico
    private static void logInicioDeProcesso(String processo, Object valor) {
        logger.info("[INÍCIO] Iniciando processo de {}: {}", processo, valor);
    }

    private static void logInicioDeProcessoAssociacao(String processo, Object valor1,  Object valor2) {
        logger.info("[INÍCIO] Iniciando processo de {}: receita {} e insumo: {}", processo, valor1, valor2);
    }

    // Insumo
    public static void logInicioCadastroDoInsumo(String nome) {
        logInicioDeProcesso("cadastro do insumo", nome);
    }

    public static void logInicioDeObtencaoDeInsumos() {
        logInicioDeProcesso("obtenção de insumos", null);
    }

    public static void logInicioDeObtencaoDoInsumoPorId(Long insumoId) {
        logInicioDeProcesso("obtenção de insumo pelo id", insumoId);
    }

    public static void logInicioDeAtualizacaoDoInsumo(Long id) {
        logInicioDeProcesso("atualização do insumo pelo ID", id);
    }

    public static void logInicioDeDelecaoDoInsumo(Long identificador) {
        logInicioDeProcesso("deleção do insumo pelo identificador", identificador);
    }

    // Receita

    public static void logInicioCadastroDeReceita(String nome) {
        logInicioDeProcesso("cadastro da receita", nome);
    }

    public static void logInicioAtualizacaoDaReceita(Long id) {
        logInicioDeProcesso("atualização da receita pelo ID", id);
    }

    public static void logInicioDeObtencaoDaReceitaPorId(Long receitaId) {
        logInicioDeProcesso("obtenção de receita pelo id", receitaId);
    }

    // Receita-Insumo

    public static void logInicioCriacaoDeAssociacao(Long receitaId, Long insumoId) {
        logInicioDeProcessoAssociacao("criação de associação entre", receitaId, insumoId);
    }

    public static void logInicioCalculoDeGastoComInsumo(Double quantidadePorPacote, BigDecimal valorPagoPorPacote,
                                                        BigDecimal quantidadeUtilizadaInsumo) {
        logInicioDeProcesso("calculo de gasto com insumo", null);
        logger.info("Quantidade por pacote: {}", quantidadePorPacote);
        logger.info("Valor pago por pacote: {}", valorPagoPorPacote);
        logger.info("Quantidade utilizada de insumo: {}", quantidadeUtilizadaInsumo);
    }

    public static void logInicioAtualizacaoQuantidadeUtilizadaInsumo(Long receitaId, Long insumoId) {
        logInicioDeProcessoAssociacao("atualização da quantidade utilizada de insumos da associação entre", receitaId, insumoId);
    }


    // LOGS DE VALIDAÇÕES

    // Genérico
    private static void logValidacao(String campo, Object valor) {
        logger.info("[VALIDAÇÃO] Validando {}: {} ", campo, valor);
    }

    private static void logValidacao(String campo, Object valor1, Object valor2) {
        logger.info("[VALIDAÇÃO] Validando {}: receita {} e insumo {}", campo, valor1, valor2);
    }

    // Insumo
    public static void logFiltroValidacao(String procedimento, Object valor) {
        logger.info("[VALIDAÇÃO - FILTRO] Validando filtro {}: {}", procedimento, valor);
    }

    public static void logValidacaoNomeDoInsumo(String nome) {
        logValidacao("nome do insumo", nome);
    }

    public static void logValidacaoDaQuantidadeDeInsumoPorPacote(Double quantidadePorPacote) {
        logValidacao("quantidade de insumo por pacote", quantidadePorPacote);
    }

    public static void logValidacaoDoValorPagoPorPacoteDeInsumo(BigDecimal valorPagoPorPacote) {
        logValidacao("valor pago por pacote de insumo", valorPagoPorPacote);
    }

    public static void logVerificacaoDeExistenciaDoInsumo(Object identificador) {
        logValidacao("existência do insumo pelo identificador", identificador);
    }

    public static void logVerificacaoDeTotalAtualEValorASubtrair(BigDecimal totalAtual, BigDecimal valorASubtrair) {
        logger.info("[VALIDAÇÃO] Validando total atual {} e valor a subtrair {}", totalAtual, valorASubtrair);
    }

    // Receita
    public static void logValidacaoNomeDaReceita(String nome) {
        logValidacao("nome da receita", nome);
    }

    public static void logVerificacaoDeExistenciaDaReceita(Object identificador) {
        logValidacao("existência do insumo pelo identificador", identificador);
    }

    // Receita-Insumo
    public static void logVerificacaoDeQuantidadeUtilizadaDeInsumo(BigDecimal quantidadeUtilizadaInsumo) {
        logValidacao("quantidade utilizada de insumo", quantidadeUtilizadaInsumo);
    }

    public static void logVerificacaoExistenciaDeAssociacaoEntreReceitaEInsumo(Long receitaId, Long insumoId) {
        logValidacao("existência de associação entre", receitaId, insumoId);
    }


    //LOGS DE SUCESSOS

    //Genérico

    private static void logSucesso(String procedimento, Object valor) {
        logger.info("[SUCESSO] Sucesso ao {}: {}", procedimento, valor);
    }

    private static void logSucesso(String procedimento, Object valor1, Object valor2) {
        logger.info("[SUCESSO] Sucesso ao {}: receita {} e insumo {}", procedimento, valor1, valor2);
    }

    // Insumo
    public static void logSucessoAoCadastrarInsumo(String nome) {
        logSucesso("cadastrar o insumo", nome);
    }

    public static void  logSucessoAoObterListaDeInsumos() {
        logSucesso("obter lista de insumos",  null);
    }

    public static void logSucessoAoVerificarExistenciaDoInsumo(Long id) {
        logSucesso("verificar existência do insumo pelo ID", id);
    }

    public static void logSucessoAoAtualizarInsumo(Object identificador) {
        logSucesso("atualizar insumo pelo identificador", identificador);
    }

    public static void logSucessoAoDeletarInsumo(Object identificador) {
        logSucesso("deletar insumo pelo identificador", identificador);
    }

    public static void logSucessoAoObterInsumoPeloId(Long id) {
        logSucesso("obter insumo pelo id", id);
    }

    // Receita
    public static void logSucessoAoCadastrarReceita(String nome) {
        logSucesso("cadastrar a receita", nome);
    }

    public static void logSucessoAoAtualizarReceita(Object identificador) {
        logSucesso("atualizar receita pelo identificador", identificador);
    }

    public static void logSucessoAoVerificarExistenciaDaReceita(Long id) {
        logSucesso("verificar existência da receita pelo ID", id);
    }

    public static void logSucessoAoObterReceitaPeloId(Long id) {
        logSucesso("obter receita pelo id", id);
    }

    // Receita-Insumo
    public static void logSucessoAoCriarAssociacaoEntreReceitaEInsumo(Long receitaId, Long insumoId) {
        logSucesso("criar associação entre", receitaId, insumoId);
    }

    public static void logSucessoAoVerificarExistenciaDaAssociacaoDeReceitaEInsumo(Long receitaId, Long insumoId) {
        logSucesso("verificar existência da associação entre", receitaId, insumoId);
    }

    public static void logSucessoAoAtualizarReceitaInsumo(Long receitaId, Long insumoId) {
        logSucesso("atualizar informações sobre a associação entre", receitaId, insumoId);
    }

    public static void logSucessoAoObterListaDeInsumosAssociadosAReceitasPeloId(Long insumoId) {
        logSucesso("obter lista de insumos associados à receitas pelo id", insumoId);
    }

    public static void logSucessoAoObterTodosOsDadosDaAssociacao(Long receitaId, Long insumoId) {
        logSucesso("obter todos os dados da associação entre", receitaId , insumoId);
    }

    //LOGS DE ERROS

    //Genérico
    private static void logErroInesperado(String procedimento, Object identificador, Exception excecao) {
        logger.error("[ERRO] Erro inesperado ao {} {}: {}", procedimento, identificador, excecao.getMessage(), excecao);
    }

    private static void logErroInesperado(String procedimento, Object identificador1, Object identificador2, Exception excecao) {
        logger.error("[ERRO] Erro inesperado ao {} receita {} e insumo {}: {}", procedimento, identificador1, identificador2, excecao.getMessage(), excecao);
    }

    // Insumo
    public static void logErroInesperadoAoCadastrarInsumo(String nome, Exception excecao) {
        logErroInesperado("cadastrar o insumo", nome, excecao);
    }

    public static void logErroInesperadoAoObterListaDeInsumos(Exception excecao) {
        logErroInesperado("obter lista de insumos", null, excecao);
    }

    public static void logErroInesperadoAoVerificarExistenciaDoInsumo(Long id,Exception excecao) {
        logErroInesperado("verificar existência do insumo pelo id", id, excecao);
    }

    public static void logErroInesperadoAoAtualizarInsumo(Object identificador, Exception excecao) {
        logErroInesperado("atualizar insumo pelo identificador", identificador, excecao);
    }

    public static void logErroInesperadoAoDeletarInsumo(Object identificador, Exception excecao) {
        logErroInesperado("deletar insumo pelo identificador", identificador, excecao);
    }

    public static void logErroInesperadoAoObterInsumoPeloId(Object identificador, Exception excecao) {
        logErroInesperado("obter insumo pelo id", identificador, excecao);
    }

    // Receita
    public static void logErroInesperadoAoCadastrarReceita(String nome, Exception excecao) {
        logErroInesperado("cadastrar a receita", nome, excecao);
    }

    public static void logErroInesperadoAoVerificarExistenciaDaReceita(Object identificador, Exception excecao) {
        logErroInesperado("verificar existência da receita pelo identificador", identificador, excecao);
    }

    public static void logErroInesperadoAoAtualizarReceita(Object identificador, Exception excecao) {
        logErroInesperado("atualizar receita pelo identificador", identificador, excecao);
    }

    public static void logErroInesperadoAoObterReceitaPeloId(Object identificador, Exception excecao) {
        logErroInesperado("obter receita pelo id", identificador, excecao);
    }

    // Receita-Insumo
    public static void logErroInesperadoAoCriarAssociacaoEntreReceitaEInsumo(Long receitaId, Long insumoId, Exception excecao) {
        logErroInesperado("criar associação entre", receitaId, insumoId, excecao);
    }

    public static void logErroInesperadoAoVerificarExistenciaDaAssociacaoDeReceitaEInsumo(Long receitaId, Long insumoId, Exception excecao) {
        logErroInesperado("verificar existência da associação entre", receitaId, insumoId, excecao);
    }

    public static void logErroInesperadoAoObterListaDeInsumosAssociadosAReceitasPeloId(Long insumoId, Exception excecao) {
        logErroInesperado("obter lista de insumos associados à receitas pelo id", insumoId, excecao);
    }

    public static void logErroInesperadoAoAtualizarReceitaInsumo(Long receitaId, Long insumoId, Exception excecao) {
        logErroInesperado("Erro ao atualizar informações sobre a associação entre", receitaId, insumoId, excecao);
    }

    public static void logErroInesperadoAoObterTodosOsDadosDaAssociacao(Long receitaId, Long insumoId, Exception excecao) {
        logErroInesperado("obter todos dados da associacao entre", receitaId, insumoId, excecao);
    }


}
