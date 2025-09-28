package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "produtos")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 200)
    @Column(length = 200, nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT") // Permite textos mais longos para descrição
    private String descricao;

    @NotNull(message = "O preço não pode ser nulo.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    @Column(nullable = false, precision = 10, scale = 2) // Ex: 99999999.99
    private BigDecimal preco;

    @NotNull(message = "O estoque não pode ser nulo.")
    @PositiveOrZero(message = "O estoque não pode ser negativo.")
    @Column(nullable = false)
    private Integer estoque;

    private String imagemPrincipalUrl;

    @Column(nullable = false)
    private LocalDate dtCriacao;
    
    // Relacionamento com Loja (Muitos Produtos para Uma Loja)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    // Relacionamento com Categoria (Muitos Produtos para Muitas Categorias)
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "produto_categoria",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
    
    public Produto() {
        this.dtCriacao = LocalDate.now();
    }

    public Produto(String nome, String descricao, BigDecimal preco, Integer estoque, Loja loja) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.loja = loja;
        this.dtCriacao = LocalDate.now();
    }

    public void adicionarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getProdutos().add(this);
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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public String getImagemPrincipalUrl() {
        return imagemPrincipalUrl;
    }

    public void setImagemPrincipalUrl(String imagemPrincipalUrl) {
        this.imagemPrincipalUrl = imagemPrincipalUrl;
    }

    public LocalDate getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(LocalDate dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public Set<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(Set<Categoria> categorias) {
        this.categorias = categorias;
    }
}