package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.time.Instant;
import java.util.Map;

public record ApiStatusDTO(
    String status,
    String versao,
    Instant timestamp,
    Map<String, String> links
) {}