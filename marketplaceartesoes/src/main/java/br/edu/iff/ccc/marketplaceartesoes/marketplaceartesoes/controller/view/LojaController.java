package br.edu.iff.ccc.marketplaceartesoes.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lojas")
public class LojaController {

    @GetMapping
    public String listarLojas() {
        return "lojas/lista";
    }

    @GetMapping("/cadastro")
    public String novaLoja() {
        return "lojas/cadastro";
    }
}
