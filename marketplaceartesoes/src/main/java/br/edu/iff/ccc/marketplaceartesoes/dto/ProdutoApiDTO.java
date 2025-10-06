package br.edu.iff.ccc.marketplaceartesoes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProdutoApiDTO(
    @NotBlank(message = "O nome do produto é obrigatório.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    String nome,

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    String descricao,

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser um valor positivo.")
    BigDecimal preco,

    @NotNull(message = "O estoque é obrigatório.")
    Integer estoque,

    @NotNull(message = "O ID da categoria é obrigatório.")
    Long categoriaId,
    
    @NotNull(message = "O ID da loja é obrigatório.")
    Long lojaId 
) {}