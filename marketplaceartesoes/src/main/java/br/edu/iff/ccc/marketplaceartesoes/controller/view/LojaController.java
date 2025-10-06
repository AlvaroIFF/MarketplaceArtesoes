package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.service.LojaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;

    @Autowired
    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }

    @GetMapping("/{id}")
    public String verLoja(@PathVariable("id") Long id, Model model) {
        try {
            model.addAttribute("loja", lojaService.buscarLojaPorId(id));
            return "loja"; 
        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }
}