package br.edu.iff.ccc.marketplaceartesoes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ArtesaoApiCadastroDTO(
    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos.")
    String cpf,

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    LocalDate dtNasc,

    String numContato,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    String senha,

    // Dados da Loja
    @NotBlank(message = "O nome da loja é obrigatório.")
    String nomeLoja,

    @Size(max = 500, message = "A descrição da loja deve ter no máximo 500 caracteres.")
    String descricaoLoja,

    @Pattern(regexp = "^$|\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos, se preenchido.")
    String cnpj
) {}