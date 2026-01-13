package br.com.patterns.microservice_patterns.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoClient {

    // O Circuit Breaker deve ficar AQUI, nesta chamada "externa"
    @CircuitBreaker(name = "gatewayPagamento", fallbackMethod = "pagamentoFallback")
    public void realizarCobrancaExterna() {
        System.out.println("[Gateway] Tentando contato com banco externo...");

        // SIMULAÇÃO DE ERRO:
        if (Math.random() > 0.5) {
            throw new RuntimeException("Timeout no Banco Externo!");
        }
        System.out.println("[Gateway] -> Sucesso! Cartão cobrado.");
    }

    public void pagamentoFallback(Exception e) {
        System.out.println("[Gateway] -> [CIRCUIT BREAKER ABERTO/ERRO] Fallback acionado. O banco está fora.");
        // Lançamos uma exceção para o PaymentService saber que deu errado
        throw new RuntimeException("Serviço de Pagamento Indisponível");
    }
}