package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ProdutoService produtoService;

    @Autowired
    public HomeController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String home(Model model) {
        // 1. Chama o serviço para buscar os produtos
        List<ProdutoDTO> produtos = produtoService.listarProdutosEmDestaque();
        
        // 2. Adiciona a lista de produtos ao "model", que será acessível no Thymeleaf
        model.addAttribute("produtos", produtos);
        
        // 3. Retorna o nome do arquivo HTML (sem a extensão .html) que deve ser renderizado
        return "index"; // Assumindo que seu arquivo se chama index.html
    }
}