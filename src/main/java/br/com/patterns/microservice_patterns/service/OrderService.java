package br.com.patterns.microservice_patterns.service;

import br.com.patterns.microservice_patterns.domain.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class OrderService {

    private final Map<Long, Pedido> repository = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher publisher;

    public OrderService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Pedido criarPedido(Long id, double valor) {
        Pedido p = new Pedido(id, valor);
        repository.put(id, p);

        System.out.println("[OrderService] 1. Pedido " + id + " criado. Status: PENDENTE");

        // PADRÃO 1: PUB/SUB (Desacoplamento)
        publisher.publishEvent(new PedidoCriadoEvent(p.getId(), p.getValor()));

        return p;
    }

    // PADRÃO 3: SAGA (Orquestração/Compensação)
    @EventListener
    public void compensarPedido(FalhaEstoqueEvent event) {
        Pedido p = repository.get(event.pedidoId());
        if (p != null) {
            p.setStatus(StatusPedido.CANCELADO);
            System.out.println("[OrderService] X. SAGA COMPENSADA: Pedido " + p.getId() + " cancelado (Sem Estoque).");
        }
    }

    public void confirmarPedido(Long id) {
        Pedido p = repository.get(id);
        if(p != null) {
            p.setStatus(StatusPedido.CONFIRMADO);
            System.out.println("[OrderService] 4. SUCESSO FINAL: Pedido " + id + " confirmado.");
        }
    }

    public Pedido buscarPedido(Long id) {
        return repository.get(id);
    }
}