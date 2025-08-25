package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.util.List;

public class Loja implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String descricao;
    private Artesao artesao;
    private List<Produto> produtos;

    public Loja() {}

    public Loja(Long id, String nome, String descricao, Artesao artesao, List<Produto> produtos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.artesao = artesao;
        this.produtos = produtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Artesao getArtesao() {
        return artesao;
    }

    public void setArtesao(Artesao artesao) {
        this.artesao = artesao;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
