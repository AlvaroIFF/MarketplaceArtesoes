package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Categoria;

public record CategoriaDTO(Long id, String nome) {

    public CategoriaDTO(Categoria categoria) {
        this(categoria.getId(), categoria.getNome());
    }
}