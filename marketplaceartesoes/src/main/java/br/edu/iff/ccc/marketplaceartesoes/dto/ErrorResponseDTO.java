package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.time.Instant;

public record ErrorResponseDTO(
    String titulo,
    int status,
    String detalhe,
    Instant timestamp
) {}