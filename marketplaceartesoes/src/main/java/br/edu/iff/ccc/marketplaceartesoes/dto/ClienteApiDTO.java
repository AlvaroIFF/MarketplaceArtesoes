package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;

public record ClienteApiDTO(
    Long id,
    String nome,
    String email,
    String numContato
) {
    public ClienteApiDTO(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getNumContato());
    }
}