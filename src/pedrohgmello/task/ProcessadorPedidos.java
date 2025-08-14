package pedrohgmello.task;

import pedrohgmello.domain.*;
import pedrohgmello.services.*;

public class ProcessadorPedidos implements Runnable {

    private Pedido pedido;
    private EstoqueService estoqueService;

    public ProcessadorPedidos(Pedido pedido, EstoqueService estoqueService) {
        this.pedido = pedido;
        this.estoqueService = estoqueService;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Processando pedido " + this.pedido.getId() + "...");
        boolean processoBaixaDeEstoque = estoqueService.processarBaixaDeEstoque(pedido);

        if (processoBaixaDeEstoque) {
            System.out.println("LOG: Verificação OK para o pedido " + pedido.getId() + ". Dando baixa no estoque.");
            pedido.setStatus(StatusPedido.PROCESSADO);
        } else {
            System.out.println("LOG: Verificação falhou para o pedido " + pedido.getId() + ". Estoque insuficiente.");
            pedido.setStatus(StatusPedido.ERRO_ESTOQUE);
        }

    }
}