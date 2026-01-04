package br.com.patterns.microservice_patterns.domain;

public class Pedido {
    private Long id;
    private double valor;
    private StatusPedido status;

    public Pedido(Long id, double valor) {
        this.id = id;
        this.valor = valor;
        this.status = StatusPedido.PENDENTE;
    }

    public Long getId() { return id; }
    public double getValor() { return valor; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    @Override
    public String toString() {
        return "Pedido{id=" + id + ", status=" + status + "}";
    }
}