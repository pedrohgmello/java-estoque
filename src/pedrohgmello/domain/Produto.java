package pedrohgmello.domain;

import java.math.BigDecimal;

public record Produto(int id, String nome, BigDecimal preco) {

    public String getNome() {
        return this.nome;
    }

    public int getId() {
        return this.id;
    }

    public BigDecimal getPreco() {
        return this.preco;
    }

}