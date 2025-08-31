package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.math.BigDecimal;
import java.util.Set;

public record ProdutoDetalheDTO(
    Long id,
    String nome,
    String descricao,
    String imagemUrl,
    BigDecimal preco,
    Integer estoque,
    String nomeArtesao,
    String nomeLoja,
    Set<String> categorias
) {}