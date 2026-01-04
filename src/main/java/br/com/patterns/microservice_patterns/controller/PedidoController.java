package br.com.patterns.microservice_patterns.controller;

import br.com.patterns.microservice_patterns.domain.Pedido;
import br.com.patterns.microservice_patterns.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final OrderService service;

    public PedidoController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public String criarPedido(@RequestParam Long id, @RequestParam double valor) {
        service.criarPedido(id, valor);
        return "Processamento iniciado para o pedido " + id;
    }

    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) {
        return service.buscarPedido(id);
    }
}