package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.math.BigDecimal;
import java.util.List;

public record CarrinhoDTO(
    List<ItemCarrinhoDTO> itens,
    BigDecimal valorTotal,
    int totalItens
) {}