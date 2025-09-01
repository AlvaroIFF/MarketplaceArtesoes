package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.math.BigDecimal;

public record ProdutoCadastroDTO(
    String nome,
    String descricao,
    BigDecimal preco,
    Integer estoque,
    String imagemUrl,
    Long categoriaId 
) {}