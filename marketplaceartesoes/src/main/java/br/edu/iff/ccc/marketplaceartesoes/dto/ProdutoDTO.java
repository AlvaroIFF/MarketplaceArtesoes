package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Produto; 
import java.math.BigDecimal;

public record ProdutoDTO(
    Long id,
    String nome,
    String imagemUrl,
    BigDecimal preco,
    String nomeArtesao,
    Integer estoque
) {
    public ProdutoDTO(Produto produto) {
        this(
            produto.getId(),
            produto.getNome(),
            produto.getImagemPrincipalUrl(),
            produto.getPreco(),
            produto.getLoja() != null && produto.getLoja().getArtesao() != null 
                ? produto.getLoja().getArtesao().getNome() 
                : null,
            produto.getEstoque()
        );
    }
}