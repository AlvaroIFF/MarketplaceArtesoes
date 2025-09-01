package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.time.LocalDate;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
@DiscriminatorValue("ARTESAO") // Rótulo para esta classe na tabela 'usuarios'
public class Artesao extends Usuario {

    @OneToOne(mappedBy = "artesao", cascade = CascadeType.ALL, orphanRemoval = true)
    private Loja loja;

    public Artesao() {
        super();
    }

    public Artesao(String nome, String cpf, LocalDate dtNasc, String numContato, String email, String senha, String fotoUrl) {
        super(nome, cpf, dtNasc, numContato, email, senha, fotoUrl);
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }
}