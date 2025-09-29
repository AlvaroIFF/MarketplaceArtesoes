package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoApiDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoUpdateDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Categoria;
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.EstoqueInsuficienteException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.marketplaceartesoes.repository.CategoriaRepository;
import br.edu.iff.ccc.marketplaceartesoes.repository.LojaRepository;
import br.edu.iff.ccc.marketplaceartesoes.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final LojaRepository lojaRepository;


    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, LojaRepository lojaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.lojaRepository = lojaRepository;
    }

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

        if (dados.categoriaId() != null) {
            categoriaRepository.findById(dados.categoriaId())
                    .ifPresent(novoProduto::adicionarCategoria);
        }
        
        produtoRepository.save(novoProduto);
    }

    @Transactional
    public ProdutoDTO atualizarProduto(Long produtoId, Long artesaoId, ProdutoUpdateDTO dadosUpdate) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        if (!produto.getLoja().getArtesao().getId().equals(artesaoId)) {
            throw new RegraDeNegocioException("Você não tem permissão para editar este produto.");
        }

        produto.setNome(dadosUpdate.nome());
        produto.setDescricao(dadosUpdate.descricao());
        produto.setPreco(dadosUpdate.preco());
        produto.setEstoque(dadosUpdate.estoque());

        if (dadosUpdate.categoriaId() != null) {
            Categoria novaCategoria = categoriaRepository.findById(dadosUpdate.categoriaId())
                    .orElseThrow(() -> new RegraDeNegocioException("A categoria selecionada não foi encontrada."));
            
            produto.getCategorias().clear();
            produto.adicionarCategoria(novaCategoria);
        }

        Produto produtoAtualizado = produtoRepository.save(produto);

        return converterParaDTO(produtoAtualizado);
    }

    @Transactional
    public void excluirProduto(Long produtoId, Long artesaoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        if (!produto.getLoja().getArtesao().getId().equals(artesaoId)) {
            throw new RegraDeNegocioException("Você não tem permissão para remover este produto.");
        }

        produtoRepository.delete(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarProdutosEmDestaque() {
        return produtoRepository.findProdutosEmDestaque(PageRequest.of(0, 3)).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarTodos(Optional<String> nomeCategoria) {
        List<Produto> produtos;

        if (nomeCategoria.isPresent() && !nomeCategoria.get().isBlank()) {
            produtos = produtoRepository.findByCategoriaNome(nomeCategoria.get()); 
        } else {
            produtos = produtoRepository.findAll();
        }

        return produtos.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoDetalheDTO buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .map(this::converterParaDetalheDTO)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }

    @Transactional(readOnly = true)
    public Optional<Produto> buscarEntidadeProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Produto buscarEntidadePorId(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));
    }

    @Transactional
    public void decrementarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));

        if (produto.getEstoque() < quantidade) {
            throw new EstoqueInsuficienteException(produto.getNome(), produto.getEstoque());
        }

        produto.setEstoque(produto.getEstoque() - quantidade);
        produtoRepository.save(produto);
    }

    @Transactional
    public void incrementarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId));
        produto.setEstoque(produto.getEstoque() + quantidade);
        produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarPorArtesaoId(Long artesaoId) {
        List<Produto> produtosDoArtesao = produtoRepository.findByArtesaoId(artesaoId); 
        
        return produtosDoArtesao.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    private ProdutoDTO converterParaDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getImagemPrincipalUrl(),
                produto.getPreco(),
                produto.getLoja().getArtesao().getNome(),
                produto.getEstoque()
        );
    }

    private ProdutoDetalheDTO converterParaDetalheDTO(Produto produto) {
    // Coleta os nomes das categorias associadas ao produto
        Set<String> nomesCategoria = produto.getCategorias().stream()
            .map(Categoria::getNome)
            .collect(Collectors.toSet());

        // Verificações para evitar NullPointerException
            Long lojaId = (produto.getLoja() != null) ? produto.getLoja().getId() : null;
        String nomeLoja = (produto.getLoja() != null) ? produto.getLoja().getNome() : "Loja não informada";
        String nomeArtesao = (produto.getLoja() != null && produto.getLoja().getArtesao() != null) 
                         ? produto.getLoja().getArtesao().getNome() 
                         : "Artesão não informado";

        // Retorna o DTO populado com todos os dados necessários
        return new ProdutoDetalheDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getImagemPrincipalUrl(),
            produto.getPreco(),
            produto.getEstoque(),
            lojaId, 
            nomeLoja,
            nomeArtesao,
            nomesCategoria
        );
    }

    @Transactional
    public ProdutoDetalheDTO cadastrarProdutoApi(ProdutoApiDTO dto) {
        Loja loja = lojaRepository.findById(dto.lojaId())
            .orElseThrow(() -> new RegraDeNegocioException("Loja não encontrada com o ID: " + dto.lojaId()));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
            .orElseThrow(() -> new RegraDeNegocioException("Categoria não encontrada com o ID: " + dto.categoriaId()));

        Produto novoProduto = new Produto();
        novoProduto.setNome(dto.nome());
        novoProduto.setDescricao(dto.descricao());
        novoProduto.setPreco(dto.preco());
        novoProduto.setEstoque(dto.estoque());
        novoProduto.setLoja(loja);
        novoProduto.adicionarCategoria(categoria);
        novoProduto.setImagemPrincipalUrl(null); 
        
        Produto produtoSalvo = produtoRepository.save(novoProduto);
        
        return converterParaDetalheDTO(produtoSalvo);
    }
    
    @Transactional
    public ProdutoDetalheDTO atualizarProdutoApi(Long id, ProdutoApiDTO dto) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new ProdutoNaoEncontradoException(id));
        
        if (!produto.getLoja().getId().equals(dto.lojaId())) {
             throw new RegraDeNegocioException("Não é permitido alterar a loja de um produto.");
        }
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
            .orElseThrow(() -> new RegraDeNegocioException("Categoria não encontrada com o ID: " + dto.categoriaId()));

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setPreco(dto.preco());
        produto.setEstoque(dto.estoque());
        produto.getCategorias().clear();
        produto.adicionarCategoria(categoria);

        Produto produtoAtualizado = produtoRepository.save(produto);

        return converterParaDetalheDTO(produtoAtualizado);
    }

    @Transactional
    public void excluirProdutoApi(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ProdutoNaoEncontradoException(id);
        }
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProdutoDetalheDTO> buscarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::converterParaDetalheDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDetalheDTO> buscarPorCategoriaId(Long categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new RegraDeNegocioException("Categoria não encontrada com o ID: " + categoriaId);
        }
        return produtoRepository.findByCategoriaId(categoriaId).stream()
            .map(this::converterParaDetalheDTO)
            .collect(Collectors.toList());
    }
}