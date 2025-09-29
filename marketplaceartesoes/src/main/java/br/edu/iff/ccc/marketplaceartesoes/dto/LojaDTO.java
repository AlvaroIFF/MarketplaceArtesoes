package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import java.util.List;

public record LojaDTO(
    Long id,
    String nome,
    String descricao,
    String imagemBannerUrl,
    String nomeArtesao,
    String emailArtesao,
    String contatoArtesao,
    List<ProdutoDTO> produtos 
) {
    public LojaDTO(Loja loja, List<ProdutoDTO> produtos) {
        this(
            loja.getId(),
            loja.getNome(),
            loja.getDescricao(),
            loja.getImagemBannerUrl(),
            loja.getArtesao().getNome(),
            loja.getArtesao().getEmail(),
            loja.getArtesao().getNumContato(),
            produtos
        );
    }
}