package br.edu.iff.ccc.marketplaceartesoes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClienteApiUpdateDTO(
    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    LocalDate dtNasc,

    String numContato,

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Formato de e-mail inválido.")
    String email
) {}