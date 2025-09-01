package br.edu.iff.ccc.marketplaceartesoes.dto;

// DTO para representar o artesão logado na sessão e no dashboard.
public record ArtesaoDTO(
    Long id,
    String nome,
    String email,
    String nomeLoja,
    String descricaoLoja
) {}