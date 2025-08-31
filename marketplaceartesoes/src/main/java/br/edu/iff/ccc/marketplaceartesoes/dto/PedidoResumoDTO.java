package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para exibir na lista de "Meus Pedidos"
public record PedidoResumoDTO(
    Long id,
    LocalDateTime dataPedido,
    BigDecimal valorTotal,
    String status
) {}