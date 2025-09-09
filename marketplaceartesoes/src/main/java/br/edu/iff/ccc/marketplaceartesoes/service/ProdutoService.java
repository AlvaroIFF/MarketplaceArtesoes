package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Categoria;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import br.edu.iff.ccc.marketplaceartesoes.repository.CategoriaRepository;
import br.edu.iff.ccc.marketplaceartesoes.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }
    

    // Cadastra um novo produto
    @Transactional 
    public void cadastrarProduto(ProdutoCadastroDTO dados, Artesao artesaoLogado) {
        Loja lojaDoArtesao = artesaoLogado.getLoja();

        Produto novoProduto = new Produto(
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.estoque(),
                lojaDoArtesao
        );
        novoProduto.setImagemPrincipalUrl(dados.imagemUrl());

        // Busca a categoria
        if (dados.categoriaId() != null) {
            categoriaRepository.findById(dados.categoriaId()).ifPresent(categoria -> {
                novoProduto.getCategorias().add(categoria);
            });
        }
        
        produtoRepository.save(novoProduto); 
    }

    // Retorna os 3 primeiros produtos para a home page
    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarProdutosEmDestaque() {
        return produtoRepository.findProdutosEmDestaque(PageRequest.of(0, 3)).stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    // Retorna todos os produtos do banco
    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarTodos(Optional<String> categoriaSlug) {
        List<Produto> produtos;
        if (categoriaSlug.isPresent()) {
            produtos = produtoRepository.findByCategoriaNome(categoriaSlug.get());
        } else {
            produtos = produtoRepository.findAll();
        }
        return produtos.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    // Busca um produto
    @Transactional(readOnly = true)
    public Optional<ProdutoDetalheDTO> buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(this::converterParaDetalheDTO); // Converte para DTO se encontrado
    }


    // Busca todos os produtos de um artesão
    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarPorArtesaoId(Long artesaoId) {
        return produtoRepository.findByArtesaoId(artesaoId).stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }

    private ProdutoDTO converterParaDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getImagemPrincipalUrl(),
                produto.getPreco(),
                produto.getLoja().getArtesao().getNome()
        );
    }

    private ProdutoDetalheDTO converterParaDetalheDTO(Produto produto) {
        Set<String> nomesCategoria = produto.getCategorias().stream()
                .map(Categoria::getNome)
                .collect(Collectors.toSet());

        return new ProdutoDetalheDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getImagemPrincipalUrl(),
                produto.getPreco(),
                produto.getEstoque(),
                produto.getLoja().getArtesao().getNome(),
                produto.getLoja().getNome(),
                nomesCategoria
        );
    }
}