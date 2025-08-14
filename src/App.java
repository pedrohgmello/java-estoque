import pedrohgmello.task.*;
import pedrohgmello.services.*;
import pedrohgmello.domain.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.*;
import java.math.BigDecimal;

public class App {
    public static void main(String[] args) {

        EstoqueService estoqueService = new EstoqueService();

        Produto camisa = new Produto(1, "Camisa", new BigDecimal("45.00"));
        Produto calca = new Produto(2, "Calça", new BigDecimal("110.00"));
        Produto meia = new Produto(3, "Meia", new BigDecimal("10.00"));

        estoqueService.popularEstoque(camisa, 10);
        estoqueService.popularEstoque(calca, 5);
        estoqueService.popularEstoque(meia, 0);

        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(new Pedido(1, List.of(camisa, camisa, calca), LocalDateTime.now()));
        pedidos.add(new Pedido(2, List.of(camisa, camisa, camisa, camisa, camisa), LocalDateTime.now()));
        pedidos.add(new Pedido(3, List.of(calca, meia), LocalDateTime.now()));
        pedidos.add(new Pedido(4, List.of(camisa, camisa, camisa, camisa), LocalDateTime.now()));

        //Sincronizar listas

        List<Thread> threads = new ArrayList<>();

        pedidos.forEach(pedido -> {
            threads.add(new Thread(new ProcessadorPedidos(pedido, estoqueService)));

        });

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("ERRO INESPERADO");
                System.err.println(e);
            }
        }

        System.out.println(estoqueService.getEstoque());

        BigDecimal totalProcessados = pedidos.stream()
                .filter(pedido -> pedido.getStatus() == StatusPedido.PROCESSADO)
                .flatMap(pedido -> pedido.getItens().stream())
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, (subtotal, preco) -> subtotal.add(preco));

        Map<StatusPedido, Long> relatorioStatus = pedidos.stream()
                .collect(Collectors.groupingBy(Pedido::getStatus, Collectors.counting()));

        Optional<Pedido> maisCaro = pedidos.stream()
                .filter(pedido -> pedido.getStatus() == StatusPedido.PROCESSADO)
                .max(Comparator.comparing(Pedido::getPreco));

        System.out.println("O valor de compra somado de todos os pedidos: R$" + totalProcessados
                + "; Relatório de status dos pedidos: " + relatorioStatus + ".");
        if (maisCaro.isPresent()) {
            Pedido pedidoMaisCaro = maisCaro.get();
            System.out.println("--- Pedido Mais Caro Encontrado ---");
            System.out.println("Valor Total: R$ " + pedidoMaisCaro.getPreco());
        } else {
            System.out.println("Nenhum pedido processado foi encontrado para análise.");
        }

    }
}
