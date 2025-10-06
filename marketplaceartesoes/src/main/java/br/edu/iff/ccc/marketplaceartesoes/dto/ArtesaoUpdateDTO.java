package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;

import java.time.LocalDate;

public record ArtesaoUpdateDTO(
        String nome,
        String cpf,
        LocalDate dtNasc,
        String numContato,
        String email,

        String nomeLoja,
        String descricaoLoja,
        String cnpj
) {
    public ArtesaoUpdateDTO(Artesao artesao) {
        this(
            artesao.getNome(),
            artesao.getCpf(),
            artesao.getDtNasc(),
            artesao.getNumContato(),
            artesao.getEmail(),
            artesao.getLoja() != null ? artesao.getLoja().getNome() : "",
            artesao.getLoja() != null ? artesao.getLoja().getDescricao() : "",
            artesao.getLoja() != null ? artesao.getLoja().getCnpj() : ""
        );
    }
}