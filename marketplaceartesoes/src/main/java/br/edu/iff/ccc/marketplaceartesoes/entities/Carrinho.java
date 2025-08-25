package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.util.List;

public class Carrinho implements java.io.Serializable {
    
    private Long id;
    private Cliente cliente;
    private List<ItemCarrinho> itens;

    public Carrinho() {}

    public Carrinho(Long id, Cliente cliente, List<ItemCarrinho> itens) {
        this.id = id;
        this.cliente = cliente;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

}
