package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontrado;
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
        try {
            ProdutoDetalheDTO produto = produtoService.buscarPorId(id);

            model.addAttribute("produto", produto);
            return "detalhe-produto";
        } catch (ProdutoNaoEncontrado e) {
            System.err.println("Erro: " + e.getMessage());
            return "redirect:/produtos"; // Redireciona para a lista de produtos
        }
    }

    @PostMapping("/{id}/adicionar")
    public String adicionarAoCarrinho(@PathVariable("id") Long id, @RequestParam("quantidade") int quantidade) {
        try {
            carrinhoService.adicionarItem(id, quantidade);
        } catch (ProdutoNaoEncontrado e) {
            System.err.println("Tentativa de adicionar produto inexistente ao carrinho. ID: " + id);
            return "redirect:/produtos";
        }
        return "redirect:/carrinho";
    }
}