package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoResumoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente; // Importe a entidade Cliente
import br.edu.iff.ccc.marketplaceartesoes.entities.ItemPedido;
import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final List<Pedido> pedidosEmMemoria = new ArrayList<>();
    private static final AtomicLong idContador = new AtomicLong(1);
    
    // Injeção do ClienteService é necessária para buscar a entidade
    private final ClienteService clienteService;

    public PedidoService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Cria um novo pedido a partir de um carrinho e de um cliente.
     */
    public Pedido criarPedido(CarrinhoDTO carrinho, ClienteDTO clienteDto) {
        if (carrinho.itens().isEmpty()) {
            throw new IllegalStateException("Não é possível criar um pedido com o carrinho vazio.");
        }
    
        // Busca a entidade Cliente completa a partir do ID do DTO
        Cliente clienteEntidade = clienteService.buscarEntidadePorId(clienteDto.id());

        Pedido novoPedido = new Pedido();
        novoPedido.setId(idContador.getAndIncrement());
        novoPedido.setValorTotal(carrinho.valorTotal());
    
        // Associa a entidade Cliente ao pedido (Lado 1 da relação)
        novoPedido.setCliente(clienteEntidade);
        // Adiciona o novo pedido à lista de pedidos do cliente
        clienteEntidade.getPedidos().add(novoPedido);
 
        /* 
        // Associa o endereço (vamos pegar o primeiro da lista do cliente como padrão)
        if (clienteEntidade.getEnderecos() != null && !clienteEntidade.getEnderecos().isEmpty()) {
            novoPedido.setEnderecoDeEntrega(clienteEntidade.getEnderecos().get(0));
        } else {
            // Se o cliente não tiver endereço, não podemos finalizar a compra.
            // Em um sistema real, redirecionaríamos para a página de cadastro de endereço.
            throw new IllegalStateException("Cliente não possui endereço de entrega cadastrado.");
        }
        */
        
        carrinho.itens().forEach(itemCarrinho -> {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPrecoUnitario(itemCarrinho.precoUnitario());
            itemPedido.setQuantidade(itemCarrinho.quantidade());
            itemPedido.setPedido(novoPedido);
            novoPedido.getItens().add(itemPedido);
        });

        pedidosEmMemoria.add(novoPedido);
        System.out.println("Pedido criado com sucesso para o Cliente " + clienteEntidade.getNome() + "! Pedido ID: " + novoPedido.getId());
        return novoPedido;
    }

    /**
     * Busca todos os pedidos de um cliente específico.
     */
    public List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId) {
        return pedidosEmMemoria.stream()
                .filter(pedido -> pedido.getCliente() != null && pedido.getCliente().getId().equals(clienteId))
                .map(this::converterParaResumoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte uma entidade Pedido para um DTO de resumo.
     */
    private PedidoResumoDTO converterParaResumoDTO(Pedido pedido) {
        return new PedidoResumoDTO(
                pedido.getId(),
                pedido.getDtPedido(),
                pedido.getValorTotal(),
                pedido.getStatus().toString()
        );
    }
}