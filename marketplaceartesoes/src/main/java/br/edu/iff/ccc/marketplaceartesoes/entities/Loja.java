package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "lojas")
public class Loja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da loja é obrigatório.")
    @Size(max = 150)
    @Column(length = 150, nullable = false, unique = true)
    private String nome;

    @Size(max = 500)
    @Column(length = 500)
    private String descricao;

    @Size(min = 14, max = 14, message = "CNPJ deve ter 14 dígitos.")
    @Column(length = 14, unique = true)
    private String cnpj;

    private String imagemBannerUrl; 

    @Column(nullable = false)
    private LocalDate dtCriacao;

    // Relacionamento com Artesao (Loja é a dona)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artesao_id", nullable = false, unique = true)
    private Artesao artesao;

    // Relacionamento com Produto
    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    // Construtor vazio para o JPA
    public Loja() {
        this.dtCriacao = LocalDate.now(); // Define a data de criação automaticamente
    }

    // Construtor com campos essenciais
    public Loja(String nome, String descricao, String cnpj, Artesao artesao) {
        this.nome = nome;
        this.descricao = descricao;
        this.cnpj = cnpj;
        this.artesao = artesao;
        this.dtCriacao = LocalDate.now();
    }

    // Getters e Setters
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getImagemBannerUrl() {
        return imagemBannerUrl;
    }

    public void setImagemBannerUrl(String imagemBannerUrl) {
        this.imagemBannerUrl = imagemBannerUrl;
    }

    public LocalDate getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDate dtCriacao) {
        this.dtCriacao = dtCriacao;
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