package br.com.patterns.microservice_patterns.service;

import br.com.patterns.microservice_patterns.domain.PedidoCriadoEvent;
import br.com.patterns.microservice_patterns.domain.PagamentoAprovadoEvent;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final ApplicationEventPublisher publisher;

    public PaymentService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener
    public void processarPagamento(PedidoCriadoEvent event) {
        try {
            System.out.println("[PaymentService] 2. Tentando cobrar cartão...");
            realizarCobrancaExterna();

            // Se passar pelo Circuit Breaker:
            publisher.publishEvent(new PagamentoAprovadoEvent(event.pedidoId()));

        } catch (Exception e) {
            System.out.println("[PaymentService] ERRO: " + e.getMessage());
            // Aqui poderíamos disparar um evento de "FalhaPagamento" se quiséssemos
        }
    }

    // PADRÃO 2: CIRCUIT BREAKER
    @CircuitBreaker(name = "gatewayPagamento", fallbackMethod = "pagamentoFallback")
    public void realizarCobrancaExterna() {
        // SIMULAÇÃO DE ERRO: 30% de chance de falha no banco
        if (Math.random() > 1.1) {
            throw new RuntimeException("Timeout no Banco Externo!");
        }
        System.out.println("[PaymentService] -> Cartão cobrado com sucesso.");
    }

    public void pagamentoFallback(Exception e) {
        System.out.println("[PaymentService] -> [CIRCUIT BREAKER ABERTO] Serviço protegido. Erro ignorado.");
    }
}