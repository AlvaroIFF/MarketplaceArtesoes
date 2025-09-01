package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.math.BigDecimal;

public record ItemCarrinhoDTO(
    Long produtoId,
    String nomeProduto,
    BigDecimal precoUnitario,
    int quantidade,
    String imagemUrl
) {
    // Método para calcular o subtotal deste item
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(new BigDecimal(quantidade));
    }
}