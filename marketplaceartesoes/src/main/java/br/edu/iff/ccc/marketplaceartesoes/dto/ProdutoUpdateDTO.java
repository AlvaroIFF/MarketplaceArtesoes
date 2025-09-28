package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; 
import java.math.BigDecimal;

public record ProdutoUpdateDTO(
        Long id, 

        @NotBlank(message = "O nome do produto é obrigatório.")
        String nome,

        String descricao,

        @NotNull(message = "O preço é obrigatório.")
        @PositiveOrZero(message = "O preço deve ser um valor positivo ou zero.")
        BigDecimal preco,

        @NotNull(message = "O estoque é obrigatório.")
        @PositiveOrZero(message = "O estoque não pode ser um valor negativo.")
        Integer estoque,
        
        Long categoriaId
) {
    public ProdutoUpdateDTO(Produto produto) {
        this(
            produto.getId(), 
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            (produto.getCategorias() != null && !produto.getCategorias().isEmpty())
                ? produto.getCategorias().iterator().next().getId() 
                : null
        );
    }
}