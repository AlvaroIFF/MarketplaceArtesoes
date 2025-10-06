package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.service.CarrinhoService;
import br.edu.iff.ccc.marketplaceartesoes.service.CategoriaService; // <-- Adicione este import
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
@RequestMapping("/produtos") 
public class ProdutoController {

    private final ProdutoService produtoService;
    private final CarrinhoService carrinhoService;
    private final CategoriaService categoriaService; 

    @Autowired
    public ProdutoController(ProdutoService produtoService, CarrinhoService carrinhoService, CategoriaService categoriaService) { // <-- Adicione categoriaService aqui
        this.produtoService = produtoService;
        this.carrinhoService = carrinhoService;
        this.categoriaService = categoriaService; 
    }

    @GetMapping
    public String listarProdutos(@RequestParam(required = false) String categoria, Model model) {
        List<ProdutoDTO> produtos = produtoService.buscarTodos(Optional.ofNullable(categoria));
    
        model.addAttribute("categorias", categoriaService.buscarTodas());
    
        model.addAttribute("produtos", produtos);
    
        model.addAttribute("categoriaSelecionada", categoria);
    
        return "produtos";
    }


    @GetMapping("/{id}")
    public String detalheProduto(@PathVariable("id") Long id, Model model) {
        try {
            ProdutoDetalheDTO produto = produtoService.buscarPorId(id);

            model.addAttribute("produto", produto);
            return "detalhe-produto";
        } catch (ProdutoNaoEncontradoException e) {
            System.err.println("Erro: " + e.getMessage());
            return "redirect:/produtos"; // Redireciona para a lista de produtos
        }
    }

    @PostMapping("/{id}/adicionar")
    public String adicionarAoCarrinho(@PathVariable("id") Long id, @RequestParam("quantidade") int quantidade) {
        try {
            carrinhoService.adicionarItem(id, quantidade);
        } catch (ProdutoNaoEncontradoException e) {
            System.err.println("Tentativa de adicionar produto inexistente ao carrinho. ID: " + id);
            return "redirect:/produtos";
        }
        return "redirect:/carrinho";
    }

}