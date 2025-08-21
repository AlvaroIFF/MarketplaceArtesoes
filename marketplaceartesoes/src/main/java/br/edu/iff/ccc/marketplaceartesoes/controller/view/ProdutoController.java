package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @GetMapping
    public String listarProdutos() {
        return "produtos/lista";
    }

    @GetMapping("/cadastro")
    public String novoProduto() {
        return "produtos/cadastro";
    }
}
