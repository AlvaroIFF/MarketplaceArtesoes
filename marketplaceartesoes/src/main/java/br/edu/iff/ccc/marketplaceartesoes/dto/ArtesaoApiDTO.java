package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;

public record ArtesaoApiDTO(
    Long id,
    String nome,
    String email,
    Long lojaId,
    String nomeLoja
) {
    public ArtesaoApiDTO(Artesao artesao) {
        this(
            artesao.getId(),
            artesao.getNome(),
            artesao.getEmail(),
            artesao.getLoja() != null ? artesao.getLoja().getId() : null,
            artesao.getLoja() != null ? artesao.getLoja().getNome() : null
        );
    }
}