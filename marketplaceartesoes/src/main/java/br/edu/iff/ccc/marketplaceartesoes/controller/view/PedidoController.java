package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @GetMapping("/sucesso")
    public String exibirConfirmacaoPedido(@ModelAttribute("pedidoId") Long pedidoId, Model model) {
        // O @ModelAttribute pega o atributo "pedidoId" que foi adicionado
        // pelo RedirectAttributes no CheckoutController.
        
        // Adicionamos o ID ao model para que a página HTML possa exibi-lo.
        model.addAttribute("numeroPedido", pedidoId);

        return "pedido-sucesso"; // Nome do arquivo HTML
    }
}
