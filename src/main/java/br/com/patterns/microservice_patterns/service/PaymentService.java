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
    private final GatewayPagamentoClient gatewayClient; // Injeção da nova classe

    // O Spring vai injetar o Gateway aqui automaticamente
    public PaymentService(ApplicationEventPublisher publisher, GatewayPagamentoClient gatewayClient) {
        this.publisher = publisher;
        this.gatewayClient = gatewayClient;
    }

    @EventListener
    public void processarPagamento(PedidoCriadoEvent event) {
        try {
            System.out.println("[PaymentService] 2. Processando pagamento do pedido " + event.pedidoId());

            gatewayClient.realizarCobrancaExterna();

            // Se passou pelo gateway sem erro, publica o sucesso
            publisher.publishEvent(new PagamentoAprovadoEvent(event.pedidoId()));

        } catch (Exception e) {
            System.out.println("[PaymentService] ERRO CAPTURADO: " + e.getMessage());
            // O fluxo para aqui. O pedido não é confirmado.
        }
    }
}