package br.edu.iff.ccc.marketplaceartesoes.entities;

public class ItemCarrinho {
    private Long id;
    private Produto produto;
    private int quantidade;

    public ItemCarrinho() {}

    public ItemCarrinho(Long id, Produto produto, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
