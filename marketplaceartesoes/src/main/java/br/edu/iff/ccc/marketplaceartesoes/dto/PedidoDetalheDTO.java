package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import br.edu.iff.ccc.marketplaceartesoes.entities.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PedidoDetalheDTO(
    Long id,
    LocalDateTime dataPedido,
    StatusPedido status,
    BigDecimal valorTotal,
    String nomeCliente,
    String cpfCliente,
    List<ItemPedidoDTO> itens
) {
    public PedidoDetalheDTO(Pedido pedido) {
        this(
            pedido.getId(),
            pedido.getDtPedido(),
            pedido.getStatus(),
            pedido.getValorTotal(),
            pedido.getCliente() != null ? pedido.getCliente().getNome() : "Cliente não informado",
            pedido.getCliente() != null ? pedido.getCliente().getCpf() : "N/A",
            pedido.getItens().stream()
                .map(ItemPedidoDTO::new)
                .collect(Collectors.toList())
        );
    }
}