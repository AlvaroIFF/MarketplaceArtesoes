package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

    @NotNull 
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE)
    private Long id;

    @ManyToMany
    @Embedded
    private List<Endereco> endereco;

    public Cliente() {}

    public Cliente(Long id, String nome, String cpf, Date dtNasc, String numContato, String email) {
        super(id, nome, cpf, dtNasc, numContato, email);
    }

    public List<Endereco> getEndereco() {
        return endereco;
    }
    public void setEndereco(List<Endereco> endereco) {
        this.endereco = endereco;
    }
}
