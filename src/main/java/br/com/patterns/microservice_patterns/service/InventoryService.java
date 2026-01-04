package br.com.patterns.microservice_patterns.service;

import br.com.patterns.microservice_patterns.domain.PagamentoAprovadoEvent;
import br.com.patterns.microservice_patterns.domain.FalhaEstoqueEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final OrderService orderService;
    private final ApplicationEventPublisher publisher;

    public InventoryService(OrderService orderService, ApplicationEventPublisher publisher) {
        this.orderService = orderService;
        this.publisher = publisher;
    }

    @EventListener
    public void reservarIngresso(PagamentoAprovadoEvent event) {
        System.out.println("[InventoryService] 3. Verificando estoque...");

        // LÓGICA DA SAGA:
        // ID Par = Tem estoque (Sucesso)
        // ID Ímpar = Não tem estoque (Falha -> Dispara Compensação)
        boolean temEstoque = (event.pedidoId() % 2 == 0);

        if (temEstoque) {
            orderService.confirmarPedido(event.pedidoId());
        } else {
            System.out.println("[InventoryService] -> ESTOQUE INDISPONÍVEL! Iniciando rollback...");
            publisher.publishEvent(new FalhaEstoqueEvent(event.pedidoId()));
        }
    }
}