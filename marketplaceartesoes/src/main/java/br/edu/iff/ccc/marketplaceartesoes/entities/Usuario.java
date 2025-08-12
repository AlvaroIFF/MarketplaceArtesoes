package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Usuario implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @NotNull 
    @Id
    private Long id;
    
    @NotEmpty(message = "O nome do Usuário não pode ser vazio.") 
    @Size(max=100)
    private String nome;
    
    @NotEmpty(message = "O CPF deve ser fornecido") 
    @Size(max=11)
    private String cpf;

    @NotEmpty
    private Date dtNasc;

    @NotEmpty(message = "Um número de contato deve ser fornecido") 
    @Size(max=11)
    private String numContato;

    @NotEmpty(message = "Um e-mail deve ser fornecido") 
    @Email
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
