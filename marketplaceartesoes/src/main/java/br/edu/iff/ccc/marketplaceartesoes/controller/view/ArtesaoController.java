package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/artesoes")
public class ArtesaoController {

    @GetMapping
    public String listarArtesoes() {
        return "artesoes/lista";
    }

    @GetMapping("/cadastro")
    public String novoArtesao() {
        return "artesoes/cadastro";
    }
}
