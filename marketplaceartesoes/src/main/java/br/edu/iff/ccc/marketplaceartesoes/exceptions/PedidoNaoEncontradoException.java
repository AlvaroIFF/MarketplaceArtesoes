package br.edu.iff.ccc.marketplaceartesoes.exceptions;

public class PedidoNaoEncontradoException extends RuntimeException {

    public PedidoNaoEncontradoException(Long pedidoId) {
        super("Pedido não encontrado com o ID: " + pedidoId);
    }
}
