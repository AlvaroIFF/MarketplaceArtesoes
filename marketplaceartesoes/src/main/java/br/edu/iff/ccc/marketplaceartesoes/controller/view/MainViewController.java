package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/principal")
public class MainViewController {

    @GetMapping("/")
    public String getHomePage() {
        return "index.html";
    }
    
}
