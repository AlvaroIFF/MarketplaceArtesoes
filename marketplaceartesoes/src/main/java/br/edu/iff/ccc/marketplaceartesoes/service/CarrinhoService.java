package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ItemCarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope // Esta anotação instrui o Spring a criar uma instância separada desta classe para cada usuário
public class CarrinhoService {

    private final List<ItemCarrinhoDTO> itens = new ArrayList<>();

    /**
     * Adiciona um item ao carrinho. Se o item já existir, apenas incrementa a quantidade.
     */
    public void adicionarItem(ProdutoDetalheDTO produto, int quantidade) {
        // Verifica se o produto já está no carrinho
        Optional<ItemCarrinhoDTO> itemExistente = buscarItemPorProdutoId(produto.id());

        if (itemExistente.isPresent()) {
            // Se existir, atualiza a quantidade
            ItemCarrinhoDTO item = itemExistente.get();
            int novaQuantidade = item.quantidade() + quantidade;
            // Remove o antigo e adiciona o novo atualizado
            itens.remove(item);
            itens.add(new ItemCarrinhoDTO(produto.id(), produto.nome(), produto.preco(), novaQuantidade, produto.imagemUrl()));
        } else {
            // Se não existir, adiciona o novo item
            itens.add(new ItemCarrinhoDTO(produto.id(), produto.nome(), produto.preco(), quantidade, produto.imagemUrl()));
        }
    }

    /**
     * Remove um item completamente do carrinho, baseado no ID do produto.
     */
    public void removerItem(Long produtoId) {
        // Usamos um Iterator para remover um item da lista enquanto a percorremos,
        // o que evita problemas de concorrência.
        itens.removeIf(item -> item.produtoId().equals(produtoId));
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

        Optional<ItemCarrinhoDTO> itemOpt = buscarItemPorProdutoId(produtoId);
        if (itemOpt.isPresent()) {
            ItemCarrinhoDTO itemAntigo = itemOpt.get();
            // Criamos um novo item com a quantidade atualizada
            ItemCarrinhoDTO itemNovo = new ItemCarrinhoDTO(
                itemAntigo.produtoId(),
                itemAntigo.nomeProduto(),
                itemAntigo.precoUnitario(),
                novaQuantidade,
                itemAntigo.imagemUrl()
            );
            // Removemos o antigo e adicionamos o novo
            itens.remove(itemAntigo);
            itens.add(itemNovo);
        }
    }

    public void limparCarrinho() {
        itens.clear();
    }

    /**
     * Retorna um DTO com o estado atual do carrinho.
     */
    public CarrinhoDTO getCarrinho() {
        BigDecimal valorTotal = itens.stream()
                .map(ItemCarrinhoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalItens = itens.stream()
                .mapToInt(ItemCarrinhoDTO::quantidade)
                .sum();
        
        return new CarrinhoDTO(new ArrayList<>(itens), valorTotal, totalItens);
    }
    
    // Método auxiliar para buscar um item na lista
    private Optional<ItemCarrinhoDTO> buscarItemPorProdutoId(Long produtoId) {
        return itens.stream()
                .filter(item -> item.produtoId().equals(produtoId))
                .findFirst();
    }
}