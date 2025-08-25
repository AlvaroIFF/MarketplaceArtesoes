package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.util.Date;
import java.util.List;

public class Pedido implements java.io.Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private Artesao artesao;
    private List<ItemPedido> produtos;
    private Date dataPedido;
    private String status;

    public Pedido() {}

    public Pedido(Long id, Artesao artesao, List<ItemPedido> produtos, Date dataPedido, String status) {
        this.id = id;
        this.artesao = artesao;
        this.produtos = produtos;
        this.dataPedido = dataPedido;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artesao getArtesao() {
        return artesao;
    }

    public void setArtesao(Artesao artesao) {
        this.artesao = artesao;
    }

    public List<ItemPedido> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemPedido> produtos) {
        this.produtos = produtos;
    }
    
    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
