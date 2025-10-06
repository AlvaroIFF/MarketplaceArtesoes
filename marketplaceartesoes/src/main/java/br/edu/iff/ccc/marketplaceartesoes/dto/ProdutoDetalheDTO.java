package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import java.math.BigDecimal;
import java.util.Set;  

public record ProdutoDetalheDTO(
    Long id,
    String nome,
    String descricao,
    String imagemPrincipalUrl,
    BigDecimal preco,
    Integer estoque,
    Long lojaId,
    String nomeLoja,
    String nomeArtesao,
    Set<String> nomesCategoria 
) {
    public ProdutoDetalheDTO(
        Produto produto, 
        String imagemPrincipalUrl, 
        Long lojaId, 
        String nomeLoja, 
        String nomeArtesao, 
        Set<String> nomesCategoria
    ) {
        this(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            imagemPrincipalUrl, 
            produto.getPreco(),
            produto.getEstoque(),
            lojaId,
            nomeLoja,
            nomeArtesao,
            nomesCategoria
        );
    }
}