package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotNull 
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.SEQUENCE)
    private Long id;
    
    @NotEmpty(message = "O nome do Usuário não pode ser vazio.") 
    @Size(max=100)
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @NotEmpty(message = "O CPF deve ser fornecido") 
    @Size(max=11)
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @NotEmpty
    @Column(name = "dt_nasc", nullable = false)
    @Temporal(jakarta.persistence.TemporalType.DATE)
    private Date dtNasc;

    @NotEmpty(message = "Um número de contato deve ser fornecido") 
    @Size(max=11)
    @Column(name = "num_contato", nullable = false)
    private String numContato;

    @NotEmpty(message = "Um e-mail deve ser fornecido") 
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    public Usuario() {}

    public Usuario(Long id, String nome, String cpf, Date dtNasc, String numContato, String email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dtNasc = dtNasc;
        this.numContato = numContato;
        this.email = email;
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
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public Date getDtNasc() {
        return dtNasc;
    }
    public void setDtNasc(Date dtNasc) {
        this.dtNasc = dtNasc;
    }
    public String getNumContato() {
        return numContato;
    }
    public void setNumContato(String numContato) {
        this.numContato = numContato;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    

}
