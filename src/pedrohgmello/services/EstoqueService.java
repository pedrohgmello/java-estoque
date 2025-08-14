package pedrohgmello.services;

import pedrohgmello.domain.*;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.*;
import java.util.function.*;

public class EstoqueService {

    private Map<Produto, Integer> estoqueProduto = new HashMap<>();

    public void popularEstoque(Produto produto, int qtd) {
        estoqueProduto.merge(produto, qtd, Integer::sum);
    }

    public synchronized boolean processarBaixaDeEstoque(Pedido pedido) {
        Map<Produto, Long> itensContados = pedido.getItens().stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        boolean temEstoque = itensContados.entrySet().stream()
                .allMatch(entry -> {
                    Produto produto = entry.getKey();
                    int qtdNecessaria = entry.getValue().intValue();
                    int qtdEmEstoque = estoqueProduto.getOrDefault(produto, 0);
                    return qtdEmEstoque >= qtdNecessaria;
                });

        if (!temEstoque) {
            return false;
        }

        for (Map.Entry<Produto, Long> entry : itensContados.entrySet()) {
            Produto produto = entry.getKey();
            int qtdASerRemovida = entry.getValue().intValue();
            int estoqueAtual = estoqueProduto.getOrDefault(produto, 0);
            estoqueProduto.put(produto, estoqueAtual - qtdASerRemovida);
        }

        return true;
    }

    public Map<Produto, Integer> getEstoque() {
        return this.estoqueProduto;
    }
}