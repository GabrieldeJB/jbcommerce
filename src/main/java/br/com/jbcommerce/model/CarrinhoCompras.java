package br.com.jbcommerce.model;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoCompras {

    private List<Produto> itens = new ArrayList<>();

    public void adicionar(Produto produto) {
        itens.add(produto);
    }

    public void remover(Produto produto) {
        itens.removeIf(item -> item.getId() == produto.getId());
    }


    public void limpar() {
        itens.clear();
    }

    public List<Produto> getItens() {
        return itens;
    }

    public Double getTotal() {
        return itens.stream()
                    .mapToDouble(produto -> produto.getValor() != null ? produto.getValor() : 0.0)
                    .sum();
    }
}
