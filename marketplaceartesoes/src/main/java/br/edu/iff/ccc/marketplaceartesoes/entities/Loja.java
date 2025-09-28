package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "^$|\\d{14}", message = "O CNPJ deve estar vazio ou conter exatamente 14 dígitos.")
    @Column(length = 14, unique = true)
    private String cnpj;

    @Column(name = "imagem_banner_url") 
    private String imagemBannerUrl;

    @Column(nullable = false)
    private LocalDate dtCriacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artesao_id", nullable = false)
    private Artesao artesao;

    @OneToMany(mappedBy = "loja", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    public Loja() {
        this.dtCriacao = LocalDate.now();
    }

    public Loja(String nome, String descricao, String cnpj, Artesao artesao) {
        this(); 
        this.nome = nome;
        this.descricao = descricao;
        this.cnpj = cnpj;
        this.artesao = artesao;
    }

    // Getters e Setters

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loja loja = (Loja) o;
        return Objects.equals(id, loja.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}