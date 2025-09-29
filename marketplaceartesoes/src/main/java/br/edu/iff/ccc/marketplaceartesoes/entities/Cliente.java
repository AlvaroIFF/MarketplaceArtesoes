package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Carrinho carrinho;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Endereco> enderecos = new ArrayList<>();
    
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();
    
    // Construtor padrão continua obrigatório para o JPA
    public Cliente() {
        super();
    }

    // Construtor principal
    public Cliente(String nome, String cpf, LocalDate dtNasc, String numContato, String email, String senha, String fotoUrl) {
        super(nome, cpf, dtNasc, numContato, email, senha, fotoUrl);
        
        Carrinho novoCarrinho = new Carrinho();
        
        novoCarrinho.setCliente(this);
        
        this.carrinho = novoCarrinho;
    }

    public Cliente(String nome, String cpf, LocalDate dtNasc, String numContato, String email, String senha) {
        super(nome, cpf, dtNasc, numContato, email, senha, null);
    }

    // Getters e Setters continuam os mesmos...
    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}