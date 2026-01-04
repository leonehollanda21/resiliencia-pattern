package br.com.patterns.microservice_patterns.domain;

public enum StatusPedido {
    PENDENTE,
    PAGO,
    CONFIRMADO, // Sucesso
    CANCELADO   // Falha na Saga
}