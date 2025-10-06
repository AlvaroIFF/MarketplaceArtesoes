package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import java.util.List;
import java.util.stream.Collectors;

public record LojaDetalheDTO(
    Long id,
    String nome,
    String descricao,
    String imagemBannerUrl,
    String nomeArtesao,
    String emailArtesao,
    List<ProdutoDTO> produtos
) {
    public LojaDetalheDTO(Loja loja) {
        this(
            loja.getId(),
            loja.getNome(),
            loja.getDescricao(),
            loja.getImagemBannerUrl(),
            loja.getArtesao() != null ? loja.getArtesao().getNome() : null,
            loja.getArtesao() != null ? loja.getArtesao().getEmail() : null,
            loja.getProdutos().stream()
                .map(ProdutoDTO::new)
                .collect(Collectors.toList())
        );
    }
}