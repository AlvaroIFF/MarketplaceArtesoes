package br.edu.iff.ccc.marketplaceartesoes.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @GetMapping
    public String verCarrinho() {
        return "carrinho/visualizar";
    }
}
