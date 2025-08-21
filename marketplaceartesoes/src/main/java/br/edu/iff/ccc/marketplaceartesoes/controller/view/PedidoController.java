package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @GetMapping
    public String listarPedidos() {
        return "pedidos/lista";
    }

    @GetMapping("/novo")
    public String novoPedido() {
        return "pedidos/novo";
    }
}
