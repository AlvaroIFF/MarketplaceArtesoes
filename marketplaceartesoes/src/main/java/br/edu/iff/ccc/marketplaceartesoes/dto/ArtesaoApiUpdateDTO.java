package br.edu.iff.ccc.marketplaceartesoes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ArtesaoApiUpdateDTO(
    // Dados do Artesão
    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    LocalDate dtNasc,

    String numContato,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    String email,

    // Dados da Loja
    @NotBlank(message = "O nome da loja é obrigatório.")
    String nomeLoja,

    @Size(max = 500)
    String descricaoLoja,

    @Pattern(regexp = "^$|\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos, se preenchido.")
    String cnpj
) {}