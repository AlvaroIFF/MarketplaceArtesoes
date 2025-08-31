package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.CarrinhoDTO;
import br.edu.iff.ccc.marketplaceartesoes.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @Autowired
    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping
    public String exibirCarrinho(Model model) {
        // 1. Busca o estado atual do carrinho a partir do serviço de sessão
        CarrinhoDTO carrinho = carrinhoService.getCarrinho();
        
        // 2. Adiciona o DTO do carrinho ao model
        model.addAttribute("carrinho", carrinho);
        
        // 3. Retorna o nome da nova página HTML que vamos criar
        return "carrinho";
    }

    @PostMapping("/remover/{produtoId}")
    public String removerItemDoCarrinho(@PathVariable("produtoId") Long produtoId) {
        carrinhoService.removerItem(produtoId);
        // Redireciona de volta para a página do carrinho para ver a mudança
        return "redirect:/carrinho";
    }
    
    @PostMapping("/atualizar")
    public String atualizarQuantidade(
            @RequestParam("produtoId") Long produtoId, 
            @RequestParam("quantidade") int quantidade) {
        carrinhoService.atualizarQuantidade(produtoId, quantidade);
        return "redirect:/carrinho";
    }
}