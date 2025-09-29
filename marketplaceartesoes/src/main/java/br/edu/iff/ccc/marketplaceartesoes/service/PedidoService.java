package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.PedidoResumoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import br.edu.iff.ccc.marketplaceartesoes.entities.ItemPedido;
import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import br.edu.iff.ccc.marketplaceartesoes.entities.StatusPedido;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.EstoqueInsuficienteException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.PedidoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
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

    @Transactional
    public Pedido criarPedido(CarrinhoDTO carrinho, ClienteDTO clienteDto) {
        if (carrinho.itens().isEmpty()) {
            throw new RegraDeNegocioException("Não é possível criar um pedido com o carrinho vazio.");
        }

        Cliente clienteEntidade = clienteService.buscarEntidadePorId(clienteDto.id());
        
        Pedido novoPedido = new Pedido();
        novoPedido.setValorTotal(carrinho.valorTotal());
        novoPedido.setDtPedido(LocalDateTime.now());
        novoPedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        novoPedido.setCliente(clienteEntidade);

        for (var itemCarrinhoDTO : carrinho.itens()) {
            Produto produto = produtoService.buscarEntidadeProdutoPorId(itemCarrinhoDTO.produtoId())
                    .orElseThrow(() -> new ProdutoNaoEncontradoException(itemCarrinhoDTO.produtoId()));

            if (produto.getEstoque() < itemCarrinhoDTO.quantidade()) {
                throw new EstoqueInsuficienteException(produto.getNome(), produto.getEstoque());
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPrecoUnitario(itemCarrinhoDTO.precoUnitario());
            itemPedido.setQuantidade(itemCarrinhoDTO.quantidade());
            itemPedido.setProduto(produto);
            itemPedido.setPedido(novoPedido);
            
            novoPedido.getItens().add(itemPedido);
        }
        
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        for (ItemPedido item : pedidoSalvo.getItens()) {
            produtoService.decrementarEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        System.out.println("Pedido criado com sucesso para o Cliente " + clienteEntidade.getNome() + "! Pedido ID: " + pedidoSalvo.getId());
        return pedidoSalvo;
    }

    @Transactional
    public boolean removerPedido(Long pedidoId, Long clienteId) {
        // Busca o pedido
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));

        // Verifica se o pedido pertence ao cliente
        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new RegraDeNegocioException("Você não tem permissão para remover este pedido.");
        }

        // Antes de remover, devolve o estoque dos produtos
        for (ItemPedido item : pedido.getItens()) {
            produtoService.incrementarEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        // Remove o pedido
        pedidoRepository.delete(pedido);

        System.out.println("Pedido ID " + pedidoId + " removido com sucesso pelo cliente ID " + clienteId);

        return true;
    }

    @Transactional(readOnly = true)
    public List<PedidoResumoDTO> buscarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::converterParaResumoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoDetalheDTO buscarPedidoDetalhado(Long pedidoId, Long clienteId) {
        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));

        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new RegraDeNegocioException("Você não tem permissão para visualizar este pedido.");
        }

        return new PedidoDetalheDTO(pedido);
    }

    private PedidoResumoDTO converterParaResumoDTO(Pedido pedido) {
        return new PedidoResumoDTO(
                pedido.getId(),
                pedido.getDtPedido(),
                pedido.getValorTotal(),
                pedido.getStatus().toString()
        );
    }
}