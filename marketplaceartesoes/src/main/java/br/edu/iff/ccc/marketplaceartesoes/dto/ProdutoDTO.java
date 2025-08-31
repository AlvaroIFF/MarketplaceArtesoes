package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.math.BigDecimal;

public record ProdutoDTO(
    Long id,
    String nome,
    String imagemUrl,
    BigDecimal preco,
    String nomeArtesao
) {}