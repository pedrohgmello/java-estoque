package pedrohgmello.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.stream.*;

public class Pedido {

    private int id;
    private List<Produto> itens;
    private StatusPedido status;
    private LocalDateTime dataCriacao;

    public Pedido(int id, List<Produto> itens, LocalDateTime dataCriacao) {
        this.id = id;
        this.itens = itens;
        this.status = StatusPedido.PENDENTE;
        this.dataCriacao = dataCriacao;
    }

    public int getId() {
        return this.id;
    }

    public LocalDateTime getDataCriacao() {
        return this.dataCriacao;
    }

    public StatusPedido getStatus() {
        return this.status;
    }

    public List<Produto> getItens() {
        return this.itens;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public BigDecimal getPreco() {
        return this.itens.stream()
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}