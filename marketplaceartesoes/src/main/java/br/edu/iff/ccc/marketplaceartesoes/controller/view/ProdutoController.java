package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.CarrinhoService;
import br.edu.iff.ccc.marketplaceartesoes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/produtos") // Todas as URLs deste controller começarão com /produtos
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CarrinhoService carrinhoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService, CarrinhoService carrinhoService) {
        this.produtoService = produtoService;
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    public String listarProdutos(@RequestParam Optional<String> categoria, Model model) {
        // 1. Chama o serviço para buscar os produtos, passando o filtro de categoria se existir
        List<ProdutoDTO> produtos = produtoService.buscarTodos(categoria);
        
        // 2. Adiciona a lista ao model para o Thymeleaf usar
        model.addAttribute("produtos", produtos);
        
        // 3. Retorna o nome do arquivo HTML da página de produtos
        return "produtos";
    }

    @GetMapping("/{id}")
    public String detalheProduto(@PathVariable("id") Long id, Model model) {
        // 1. Chama o serviço para buscar o produto pelo ID
        Optional<ProdutoDetalheDTO> produtoOpt = produtoService.buscarPorId(id);

        // 2. Verifica se o produto foi encontrado dentro do Optional
        if (produtoOpt.isPresent()) {
            // Se sim, adiciona o DTO ao model e retorna a página de detalhes
            model.addAttribute("produto", produtoOpt.get());
            return "detalhe-produto";
        } else {
            // Se não, retorna uma página de erro (ou redireciona para a home)
            return "redirect:/produtos"; // Mais simples, redireciona para a lista
        }
    }

    @PostMapping("/{id}/adicionar")
    public String adicionarAoCarrinho(@PathVariable("id") Long id, @RequestParam("quantidade") int quantidade) {
        // Busca os detalhes do produto que queremos adicionar
        Optional<ProdutoDetalheDTO> produtoOpt = produtoService.buscarPorId(id);

        if (produtoOpt.isPresent()) {
            // Se o produto existe, chama o serviço do carrinho para adicioná-lo
            carrinhoService.adicionarItem(produtoOpt.get(), quantidade);
        }

        // Redireciona o usuário para a página de listagem de produtos após adicionar
        return "redirect:/produtos";
    }
}