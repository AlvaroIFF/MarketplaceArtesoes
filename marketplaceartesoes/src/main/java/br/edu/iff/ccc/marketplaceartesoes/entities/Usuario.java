package br.edu.iff.ccc.marketplaceartesoes.entities;

import java.io.Serializable;
import java.time.LocalDate; 

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios") // Definindo o nome da tabela explicitamente
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Estratégia de herança
@DiscriminatorColumn(name = "tipo_usuario", discriminatorType = DiscriminatorType.STRING)
public abstract class Usuario implements Serializable { 

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do Usuário não pode ser vazio.")
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String nome;

    @NotBlank(message = "O CPF deve ser fornecido.")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos.")
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;

    @NotNull(message = "A data de nascimento não pode ser nula.")
    @Past(message = "A data de nascimento deve ser no passado.")
    @Column(nullable = false)
    private LocalDate dtNasc;

    @NotBlank(message = "Um número de contato deve ser fornecido.")
    @Size(min = 10, max = 11)
    @Column(length = 11, nullable = false)
    private String numContato;

    @NotBlank(message = "Um e-mail deve ser fornecido.")
    @Email(message = "Formato de e-mail inválido.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Column(nullable = false)
    private String senha; 

    private String fotoUrl; 

    public Usuario() {}

    public Usuario(String nome, String cpf, LocalDate dtNasc, String numContato, String email, String senha, String fotoUrl) {
        this.nome = nome;
        this.cpf = cpf;
        this.dtNasc = dtNasc;
        this.numContato = numContato;
        this.email = email;
        this.senha = senha;
        this.fotoUrl = fotoUrl;
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

    public LocalDate getDtNasc() {
        return dtNasc;
    }

    public void setDtNasc(LocalDate dtNasc) {
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}