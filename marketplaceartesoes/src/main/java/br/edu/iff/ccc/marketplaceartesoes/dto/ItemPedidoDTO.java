package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.ItemPedido;
import java.math.BigDecimal;

public record ItemPedidoDTO(
    String nomeProduto,
    Integer quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal,    
    String imagemProdutoUrl
) {
    public ItemPedidoDTO(ItemPedido item) {
        this(
            item.getProduto() != null ? item.getProduto().getNome() : "Produto Removido",
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())),
            item.getProduto() != null ? item.getProduto().getImagemPrincipalUrl() : "/images/placeholder.png"
        );
    }
}