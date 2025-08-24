package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    
    @GetMapping
    public String loginCliente() {
        return "clientes/login";
    }

    @GetMapping("/cadastro")
    public String novoCliente() {
        return "clientes/cadastro";
    }
}
