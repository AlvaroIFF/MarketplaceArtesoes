package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ClienteDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import br.edu.iff.ccc.marketplaceartesoes.service.CarrinhoService;
import br.edu.iff.ccc.marketplaceartesoes.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CarrinhoService carrinhoService;
    private final PedidoService pedidoService;

    @Autowired
    public CheckoutController(CarrinhoService carrinhoService, PedidoService pedidoService) {
        this.carrinhoService = carrinhoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String processarCheckout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Pega os dados da sessão unificada
        Object usuarioLogado = session.getAttribute("usuarioLogado");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");

        // Verifica se o usuário é nulo OU se não é um CLIENTE
        if (usuarioLogado == null || !"CLIENTE".equals(tipoUsuario)) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Você precisa fazer login como cliente para finalizar a compra.");
            return "redirect:/auth/login";
        }
        
        // Se passou pela verificação, podemos converter o objeto para ClienteDTO
        ClienteDTO clienteDto = (ClienteDTO) usuarioLogado;
        
        CarrinhoDTO carrinho = carrinhoService.getCarrinho();
        if (carrinho.itens().isEmpty()) {
            return "redirect:/carrinho";
        }

        // Cria o pedido
        Pedido novoPedido = pedidoService.criarPedido(carrinho, clienteDto);

        // Limpa o carrinho
        carrinhoService.limparCarrinho();

        // Redireciona para a página de sucesso
        redirectAttributes.addFlashAttribute("pedidoId", novoPedido.getId());
        return "redirect:/pedidos/sucesso";
    }
}