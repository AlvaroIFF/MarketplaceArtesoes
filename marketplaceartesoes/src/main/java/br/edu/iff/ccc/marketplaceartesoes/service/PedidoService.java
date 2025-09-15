package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoResumoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import br.edu.iff.ccc.marketplaceartesoes.entities.ItemPedido;
import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import br.edu.iff.ccc.marketplaceartesoes.entities.StatusPedido;
import br.edu.iff.ccc.marketplaceartesoes.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; 
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoService produtoService; 

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, ClienteService clienteService, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.produtoService = produtoService; 
    }


    //Cria um novo pedido
    @Transactional 
    public Pedido criarPedido(CarrinhoDTO carrinho, ClienteDTO clienteDto) {
        if (carrinho.itens().isEmpty()) {
            throw new IllegalStateException("Não é possível criar um pedido com o carrinho vazio.");
        }

        // Busca o Cliente
        Cliente clienteEntidade = clienteService.buscarEntidadePorId(clienteDto.id());
        
        Pedido novoPedido = new Pedido();
        novoPedido.setValorTotal(carrinho.valorTotal());
        novoPedido.setDtPedido(LocalDateTime.now()); 
        novoPedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO); 
        
        // Associa a entidade Cliente ao pedido
        novoPedido.setCliente(clienteEntidade);

        carrinho.itens().forEach(itemCarrinhoDTO -> {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPrecoUnitario(itemCarrinhoDTO.precoUnitario());
            itemPedido.setQuantidade(itemCarrinhoDTO.quantidade());
            itemPedido.setPedido(novoPedido); // Associa o item ao pedido

            produtoService.buscarEntidadeProdutoPorId(itemCarrinhoDTO.produtoId())
                .ifPresentOrElse(
                    itemPedido::setProduto,
                    () -> { throw new IllegalStateException("Produto com ID " + itemCarrinhoDTO.produtoId() + " não encontrado para o pedido."); }
                );
            
            novoPedido.getItens().add(itemPedido);
        });

        // Salva o pedido no banco de dados.
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);
        
        System.out.println("Pedido criado com sucesso para o Cliente " + clienteEntidade.getNome() + "! Pedido ID: " + pedidoSalvo.getId());
        return pedidoSalvo;
    }

    /**
     * Busca todos os pedidos de um cliente específico no banco de dados.
     */
    @Transactional(readOnly = true)
        public List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId) {
            return pedidoRepository.findByClienteId(clienteId).stream() 
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