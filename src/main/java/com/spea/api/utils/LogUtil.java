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

    // Insumo
    public static void logInicioCadastroDoInsumo(String nome) {
        logInicioDeProcesso("cadastro do insumo", nome);
    }

    public static void logInicioDeObtencaoDeInsumos() {
        logInicioDeProcesso("obtenção de insumos", null);
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




    // LOGS DE VALIDAÇÕES

    // Genérico
    private static void logValidacao(String campo, Object valor) {
        logger.info("[VALIDAÇÃO] Validando {}: {} ", campo, valor);
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

    // Receita
    public static void logValidacaoNomeDaReceita(String nome) {
        logValidacao("nome da receita", nome);
    }

    public static void logVerificacaoDeExistenciaDaReceita(Object identificador) {
        logValidacao("existência do insumo pelo identificador", identificador);
    }



    //LOGS DE SUCESSOS

    //Genérico

    private static void logSucesso(String procedimento, Object valor) {
        logger.info("[SUCESSO] Sucesso ao {}: {}", procedimento, valor);
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

    //LOGS DE ERROS

    //Genérico
    private static void logErroInesperado(String procedimento, Object identificador, Exception excecao) {
        logger.error("[ERRO] Erro inesperado ao {} {}: {}", procedimento, identificador, excecao.getMessage(), excecao);
    }

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
}
