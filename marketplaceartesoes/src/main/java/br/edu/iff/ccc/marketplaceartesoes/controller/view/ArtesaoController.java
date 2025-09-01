package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.service.ArtesaoService;
import br.edu.iff.ccc.marketplaceartesoes.service.ProdutoService;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/artesao") // Todas as URLs aqui começarão com /artesao
public class ArtesaoController {

    private final ArtesaoService artesaoService;
    private static final List<Artesao> artesoesEmMemoria = new ArrayList<>();
    @SuppressWarnings("unused")
    private static final AtomicLong idContador = new AtomicLong(100);
    private final ProdutoService produtoService;

    @Autowired
    public ArtesaoController(ArtesaoService artesaoService, ProdutoService produtoService) {
        this.artesaoService = artesaoService;
        this.produtoService = produtoService;
    }

    /**
     * Método para MOSTRAR a página com o formulário de cadastro de artesão.
     */
    @GetMapping("/cadastro")
    public String exibirFormularioCadastro() {
        return "cadastro-artesao"; // Retorna o nome do arquivo HTML
    }

    /**
     * Método para PROCESSAR os dados do formulário de cadastro.
     */
    @PostMapping("/cadastrar")
    public String cadastrarArtesao(ArtesaoCadastroDTO dados, RedirectAttributes redirectAttributes) {
        try {
            artesaoService.cadastrarArtesao(dados);
            // Se o cadastro for bem-sucedido, envia uma mensagem de sucesso para a tela de login
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro de artesão realizado com sucesso! Faça o login.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            // Se houver um erro, volta para o formulário com uma mensagem de erro
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/artesao/cadastro";
        }
    }

    /**
     * Tenta autenticar um artesão.
     */
    public ArtesaoDTO fazerLogin(String email, String senha) {
        Artesao artesao = buscarPorEmail(email);
        if (artesao != null && artesao.getSenha().equals(senha)) {
            return converterParaDTO(artesao);
        }
        // Retorna nulo se o login falhar. O controller tratará o erro.
        return null;
    }

    @GetMapping("/dashboard")
    public String exibirDashboard(HttpSession session, Model model) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");

        // Protege a página: só pode acessar se for um artesão logado
        if (artesaoLogado == null || !"ARTESAO".equals(tipoUsuario)) {
            return "redirect:/auth/login";
        }

        // Busca os produtos deste artesão
        List<ProdutoDTO> produtos = produtoService.buscarPorArtesaoId(artesaoLogado.id());

        model.addAttribute("artesao", artesaoLogado);
        model.addAttribute("produtos", produtos);

        return "dashboard-artesao";
    }

    /**
    * Método para MOSTRAR a página com o formulário de cadastro de produto.
    */
    @GetMapping("/produtos/novo")
    public String exibirFormularioProduto(HttpSession session) {
        // Proteção de rota
        if (session.getAttribute("usuarioLogado") == null || !"ARTESAO".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/auth/login";
        }
        return "form-produto"; // Nome do arquivo HTML
    }

    /**
     * Método para PROCESSAR os dados do formulário e salvar o novo produto.
    */
    @PostMapping("/produtos/salvar")
    public String salvarNovoProduto(ProdutoCadastroDTO dadosProduto, HttpSession session, RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoDto = (ArtesaoDTO) session.getAttribute("usuarioLogado");

        // Proteção de rota
        if (artesaoDto == null) {
            return "redirect:/auth/login";
        }
    
        // Busca a entidade completa do artesão para associar ao produto
        Artesao artesaoEntidade = artesaoService.buscarEntidadePorId(artesaoDto.id());

        try {
            produtoService.cadastrarProduto(dadosProduto, artesaoEntidade);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
            return "redirect:/artesao/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar o produto: " + e.getMessage());
            return "redirect:/artesao/produtos/novo";
        }
    }

    /**
     * Busca uma entidade Artesao pelo seu ID.
     */
    public Artesao buscarEntidadePorId(Long id) {
        return artesoesEmMemoria.stream()
                .filter(artesao -> artesao.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    // Método auxiliar para buscar por e-mail
    private Artesao buscarPorEmail(String email) {
        return artesoesEmMemoria.stream()
                .filter(artesao -> artesao.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    // Método auxiliar para converter para DTO
    private ArtesaoDTO converterParaDTO(Artesao artesao) {
        return new ArtesaoDTO(
            artesao.getId(),
            artesao.getNome(),
            artesao.getEmail(),
            artesao.getLoja().getNome(),
            artesao.getLoja().getDescricao()
        );
    }

    
}