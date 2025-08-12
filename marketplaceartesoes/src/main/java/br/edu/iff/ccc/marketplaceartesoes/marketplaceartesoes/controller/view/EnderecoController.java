package br.edu.iff.ccc.marketplaceartesoes.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {

    @GetMapping
    public String listarEnderecos() {
        return "enderecos/lista";
    }

    @GetMapping("/cadastro")
    public String novoEndereco() {
        return "enderecos/cadastro";
    }
}
