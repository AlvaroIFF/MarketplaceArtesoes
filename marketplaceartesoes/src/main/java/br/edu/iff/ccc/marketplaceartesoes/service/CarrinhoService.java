package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ItemCarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto; 
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap; 
import java.util.Map; 

@Service
@SessionScope 
public class CarrinhoService {

    
    private final Map<Long, ItemCarrinhoDTO> itensMap = new ConcurrentHashMap<>();

    // Injeção do ProdutoService para buscar informações atualizadas do produto
    private final ProdutoService produtoService;

    @Autowired
    public CarrinhoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    /**
     * Adiciona um item ao carrinho. Se o item já existir, apenas incrementa a quantidade.
     * Busca os detalhes do produto do banco de dados para garantir informações atualizadas.
     */
    public void adicionarItem(Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser positiva.");
        }

        // 1. Buscar o produto do banco de dados para obter as informações mais recentes
        Produto produtoEntidade = produtoService.buscarEntidadeProdutoPorId(produtoId)
                                                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        // 2. Tentar encontrar o item no carrinho
        ItemCarrinhoDTO itemExistente = itensMap.get(produtoId);

        if (itemExistente != null) {
            // Se existir, atualiza a quantidade
            int novaQuantidade = itemExistente.quantidade() + quantidade;
            itensMap.put(produtoId, new ItemCarrinhoDTO(
                produtoEntidade.getId(),
                produtoEntidade.getNome(),
                produtoEntidade.getPreco(),
                novaQuantidade,
                produtoEntidade.getImagemPrincipalUrl()
            ));
        } else {
            // Se não existir, adiciona o novo item
            itensMap.put(produtoId, new ItemCarrinhoDTO(
                produtoEntidade.getId(),
                produtoEntidade.getNome(),
                produtoEntidade.getPreco(),
                quantidade,
                produtoEntidade.getImagemPrincipalUrl()
            ));
        }
    }

    /**
     * Remove um item completamente do carrinho, baseado no ID do produto.
     */
    public void removerItem(Long produtoId) {
        itensMap.remove(produtoId);
    }
    
    /**
     * Atualiza a quantidade de um item específico no carrinho.
     * Se a quantidade for 0 ou menos, o item é removido.
     */
    public void atualizarQuantidade(Long produtoId, int novaQuantidade) {
        if (novaQuantidade <= 0) {
            removerItem(produtoId);
            return;
        }

        ItemCarrinhoDTO itemExistente = itensMap.get(produtoId);

        if (itemExistente != null) {
            // Garante que pegamos os dados mais recentes do produto, especialmente o preço.
            Produto produtoEntidade = produtoService.buscarEntidadeProdutoPorId(produtoId)
                                                    .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

            itensMap.put(produtoId, new ItemCarrinhoDTO(
                produtoEntidade.getId(),
                produtoEntidade.getNome(),
                produtoEntidade.getPreco(),
                novaQuantidade,
                produtoEntidade.getImagemPrincipalUrl()
            ));
        }
        // Se o item não existir, não faz nada (ou você pode optar por lançar uma exceção)
    }

    /**
     * Limpa todos os itens do carrinho.
     */
    public void limparCarrinho() {
        itensMap.clear();
    }

    /**
     * Retorna um DTO com o estado atual do carrinho.
     */
    public CarrinhoDTO getCarrinho() {
        List<ItemCarrinhoDTO> itensLista = new ArrayList<>(itensMap.values()); // Pega todos os valores do mapa

        BigDecimal valorTotal = itensLista.stream()
                .map(ItemCarrinhoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalItens = itensLista.stream()
                .mapToInt(ItemCarrinhoDTO::quantidade)
                .sum();
        
        return new CarrinhoDTO(itensLista, valorTotal, totalItens);
    }
}