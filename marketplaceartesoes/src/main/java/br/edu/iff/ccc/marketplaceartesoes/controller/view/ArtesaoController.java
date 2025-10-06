package br.edu.iff.ccc.marketplaceartesoes.controller.view;

import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ArtesaoUpdateDTO; 
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoUpdateDTO; 
import br.edu.iff.ccc.marketplaceartesoes.dto.CategoriaDTO; // NOVO: Importar CategoriaDTO
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto; 
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ArtesaoNaoEncontradoException; 
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontradoException; 
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.marketplaceartesoes.service.ArtesaoService;
import br.edu.iff.ccc.marketplaceartesoes.service.CategoriaService; 
import br.edu.iff.ccc.marketplaceartesoes.service.FileStorageService;
import br.edu.iff.ccc.marketplaceartesoes.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/artesao")
public class ArtesaoController {

    private final ArtesaoService artesaoService;
    private final ProdutoService produtoService;
    private final FileStorageService fileStorageService;
    private final CategoriaService categoriaService; 

    @Autowired
    public ArtesaoController(ArtesaoService artesaoService, ProdutoService produtoService, FileStorageService fileStorageService, CategoriaService categoriaService) { 
        this.artesaoService = artesaoService;
        this.produtoService = produtoService;
        this.fileStorageService = fileStorageService;
        this.categoriaService = categoriaService; 
    }

    // --- Métodos de Cadastro de Artesão ---
    @GetMapping("/cadastro")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("artesaoCadastroDTO", new ArtesaoCadastroDTO("", "", null, "", "", "", "", "", "", ""));
        return "cadastro-artesao";
    }

    @PostMapping("/cadastrar")
    public String cadastrarArtesao(@ModelAttribute ArtesaoCadastroDTO dados,
                                   @RequestParam("foto") MultipartFile fotoPerfil,
                                   @RequestParam("fotoLoja") MultipartFile fotoLoja,
                                   RedirectAttributes redirectAttributes) {
        try {
            artesaoService.cadastrarArtesao(dados, fotoPerfil, fotoLoja);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cadastro de artesão realizado com sucesso! Faça o login.");
            return "redirect:/auth/login";
        } catch (RegraDeNegocioException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/artesao/cadastro";
        }
    }

    // --- Método do Dashboard do Artesão ---
    @GetMapping("/dashboard")
    public String exibirDashboard(HttpSession session, Model model) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        String tipoUsuario = (String) session.getAttribute("tipoUsuario");

        if (artesaoLogado == null || !"ARTESAO".equals(tipoUsuario)) {
            return "redirect:/auth/login";
        }

        List<ProdutoDTO> produtos = produtoService.buscarPorArtesaoId(artesaoLogado.id());

        model.addAttribute("artesao", artesaoLogado);
        model.addAttribute("produtos", produtos);

        return "dashboard-artesao";
    }

    // --- Métodos de Edição de Artesão e Loja ---
    @GetMapping("/editar")
    public String exibirFormularioEdicao(HttpSession session, Model model) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        if (artesaoLogado == null) {
            return "redirect:/auth/login";
        }

        Artesao artesaoEntidade = artesaoService.buscarEntidadePorId(artesaoLogado.id());
        model.addAttribute("artesaoUpdateDTO", new ArtesaoUpdateDTO(artesaoEntidade));
        
        return "editar-artesao";
    }

    @PostMapping("/editar/processar")
    public String processarEdicao(@ModelAttribute ArtesaoUpdateDTO dadosUpdate, 
                                  HttpSession session, 
                                  RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        if (artesaoLogado == null) {
            return "redirect:/auth/login";
        }

        try {
            ArtesaoDTO artesaoAtualizado = artesaoService.atualizarArtesao(artesaoLogado.id(), dadosUpdate);
            session.setAttribute("usuarioLogado", artesaoAtualizado);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Seus dados foram atualizados com sucesso!");
            return "redirect:/artesao/dashboard";
        } catch (RegraDeNegocioException | ArtesaoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            redirectAttributes.addFlashAttribute("artesaoUpdateDTO", dadosUpdate);
            return "redirect:/artesao/editar";
        }
    }
    
    @PostMapping("/produtos/remover/{id}")
    public String removerProduto(@PathVariable("id") Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        if (artesaoLogado == null) {
            return "redirect:/auth/login";
        }

        try {
            produtoService.excluirProduto(id, artesaoLogado.id());
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto removido com sucesso!");
            return "redirect:/artesao/dashboard";
        } catch (RegraDeNegocioException | ProdutoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/artesao/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao remover o produto: " + e.getMessage());
            return "redirect:/artesao/dashboard";
        }
    }

    @GetMapping("/produtos/novo")
    public String exibirFormularioProduto(HttpSession session, Model model) {
        // Verifica se o usuário está logado e é artesão
        if (session.getAttribute("usuarioLogado") == null || !"ARTESAO".equals(session.getAttribute("tipoUsuario"))) {
            return "redirect:/auth/login";
        }

        // Cria DTO vazio para o formulário
        model.addAttribute("produtoCadastroDTO", new ProdutoCadastroDTO("", "", null, 0, "", null));

        // Busca categorias e adiciona no Model com o nome que o template espera
        List<CategoriaDTO> todasCategorias = categoriaService.buscarTodas();
        model.addAttribute("todasCategorias", todasCategorias);

        return "form-produto";
    }

    @PostMapping("/produtos/salvar")
    public String salvarNovoProduto(@ModelAttribute ProdutoCadastroDTO dadosProduto,
                                     @RequestParam("imagemProduto") MultipartFile imagemProduto,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoDto = (ArtesaoDTO) session.getAttribute("usuarioLogado");

        if (artesaoDto == null) {
            return "redirect:/auth/login";
        }

        try {
            String imagemUrl = fileStorageService.salvarImagem(imagemProduto);

            ProdutoCadastroDTO dadosComImagem = new ProdutoCadastroDTO(
                    dadosProduto.nome(),
                    dadosProduto.descricao(),
                    dadosProduto.preco(),
                    dadosProduto.estoque(),
                    imagemUrl,
                    dadosProduto.categoriaId()
            );

            Artesao artesaoEntidade = artesaoService.buscarEntidadePorId(artesaoDto.id());
            produtoService.cadastrarProduto(dadosComImagem, artesaoEntidade);

            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
            return "redirect:/artesao/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao cadastrar o produto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("produtoCadastroDTO", dadosProduto);
            redirectAttributes.addFlashAttribute("categorias", categoriaService.buscarTodas()); 
            return "redirect:/artesao/produtos/novo";
        }
    }

    @GetMapping("/produtos/editar/{id}")
    public String exibirFormularioEdicaoProduto(@PathVariable("id") Long id, 
                                                HttpSession session, 
                                                Model model,
                                                RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        if (artesaoLogado == null) {
            return "redirect:/auth/login";
        }

        try {
            Produto produto = produtoService.buscarEntidadePorId(id);
            
            if (!produto.getLoja().getArtesao().getId().equals(artesaoLogado.id())) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Você não tem permissão para editar este produto.");
                return "redirect:/artesao/dashboard";
            }

            model.addAttribute("produtoUpdateDTO", new ProdutoUpdateDTO(produto));
            
            List<CategoriaDTO> categorias = categoriaService.buscarTodas(); // CORRIGIDO PARA CategoriaDTO
            model.addAttribute("categorias", categorias);

            return "editar-produto";
        } catch (ProdutoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Produto não encontrado.");
            return "redirect:/artesao/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar o produto para edição: " + e.getMessage());
            return "redirect:/artesao/dashboard";
        }
    }

    @PostMapping("/produtos/editar/processar")
    public String processarEdicaoProduto(@ModelAttribute ProdutoUpdateDTO dadosUpdate, 
                                         HttpSession session, 
                                         RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");
        if (artesaoLogado == null) {
            return "redirect:/auth/login";
        }

        try {
            produtoService.atualizarProduto(dadosUpdate.id(), artesaoLogado.id(), dadosUpdate);
            
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto atualizado com sucesso!");
            return "redirect:/artesao/dashboard";
        } catch (RegraDeNegocioException | ProdutoNaoEncontradoException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            redirectAttributes.addFlashAttribute("produtoUpdateDTO", dadosUpdate);
            redirectAttributes.addFlashAttribute("categorias", categoriaService.buscarTodas()); 
            return "redirect:/artesao/produtos/editar/" + dadosUpdate.id();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar o produto: " + e.getMessage());
            redirectAttributes.addFlashAttribute("produtoUpdateDTO", dadosUpdate);
            redirectAttributes.addFlashAttribute("categorias", categoriaService.buscarTodas()); 
            return "redirect:/artesao/produtos/editar/" + dadosUpdate.id();
        }
    }

    @PostMapping("/deletar-conta")
    public String deletarContaArtesao(HttpSession session, RedirectAttributes redirectAttributes) {
        ArtesaoDTO artesaoLogado = (ArtesaoDTO) session.getAttribute("usuarioLogado");

        if (artesaoLogado == null) {
            return "redirect:/auth/login";
        }

        try {
            artesaoService.deletarArtesaoE_Loja(artesaoLogado.id());
            session.invalidate(); 
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Sua conta de artesão e loja foram excluídas com sucesso.");
            return "redirect:/";
        } catch (RegraDeNegocioException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            return "redirect:/artesao/dashboard"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Ocorreu um erro ao tentar excluir sua conta de artesão: " + e.getMessage());
            e.printStackTrace(); 
            return "redirect:/artesao/dashboard";
        }
    }
}