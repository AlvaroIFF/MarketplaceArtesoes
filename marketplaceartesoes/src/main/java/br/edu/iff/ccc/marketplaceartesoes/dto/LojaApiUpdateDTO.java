package br.edu.iff.ccc.marketplaceartesoes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LojaApiUpdateDTO(
    @NotBlank(message = "O nome da loja é obrigatório.")
    @Size(max = 150)
    String nome,

    @Size(max = 500)
    String descricao,

    @Pattern(regexp = "^$|\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos, se preenchido.")
    String cnpj,

    String imagemBannerUrl
) {}