package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.time.LocalDate;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN") 
public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(String nome, String cpf, LocalDate dtNasc, String numContato, String email, String senha, String fotoUrl) {
        super(nome, cpf, dtNasc, numContato, email, senha, fotoUrl);
    }
}