package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.time.LocalDate;

public record ClienteDTO(
    Long id,
    String nome,
    String cpf,
    LocalDate dtNasc,
    String numContato,
    String email,
    String fotoUrl
) {}