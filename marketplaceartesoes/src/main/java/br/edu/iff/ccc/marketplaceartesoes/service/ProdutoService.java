package br.edu.iff.ccc.marketplaceartesoes.service;

import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoCadastroDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDTO;
import br.edu.iff.ccc.marketplaceartesoes.dto.ProdutoDetalheDTO;
import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import br.edu.iff.ccc.marketplaceartesoes.entities.Categoria; // Importe a Categoria
import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Usaremos Optional para o parâmetro
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private static final List<Produto> produtosEmMemoria = new ArrayList<>();

    static {
        // Criando Categorias
        Categoria catCeramica = new Categoria();
        catCeramica.setId(1L);
        catCeramica.setNome("Cerâmica");

        Categoria catTexteis = new Categoria();
        catTexteis.setId(2L);
        catTexteis.setNome("Têxteis");

        Categoria catJoias = new Categoria();
        catJoias.setId(3L);
        catJoias.setNome("Jóias");

        Categoria catMadeira = new Categoria();
        catMadeira.setId(4L);
        catMadeira.setNome("Madeira");

        // Artesão 1 e sua loja/produto
        Artesao artesao1 = new Artesao("Maria Silva", "11111111111", LocalDate.now().minusYears(30), "21999999991", "maria@email.com", "senha123", null);
        artesao1.setId(101L); // <-- ADICIONE ESTA LINHA
        Loja loja1 = new Loja("Ateliê da Maria", "Cerâmicas artesanais", null, artesao1);
        artesao1.setLoja(loja1); // Garante a relação bidirecional
        Produto produto1 = new Produto("Vaso de Cerâmica", "Vaso feito à mão", new BigDecimal("120.00"), 10, loja1);
        produto1.setId(1L); 
        produto1.setImagemPrincipalUrl("/images/vaso-ceramica.jpg");
        produto1.getCategorias().add(catCeramica);
        
        // Artesão 2 e sua loja/produto
        Artesao artesao2 = new Artesao("João Costa", "22222222222", LocalDate.now().minusYears(45), "11988888882", "joao@email.com", "senha123", null);
        artesao2.setId(102L); // <-- ADICIONE ESTA LINHA
        Loja loja2 = new Loja("Fios e Nós", "Bolsas e acessórios em crochê", null, artesao2);
        artesao2.setLoja(loja2); // Garante a relação bidirecional
        Produto produto2 = new Produto("Bolsa de Crochê", "Bolsa de ombro colorida", new BigDecimal("85.50"), 15, loja2);
        produto2.setId(2L);
        produto2.setImagemPrincipalUrl("/images/bolsa-croche.jpg");
        produto2.getCategorias().add(catTexteis);

        // Artesão 3 e sua loja/produto
        Artesao artesao3 = new Artesao("Ana Pereira", "33333333333", LocalDate.now().minusYears(28), "81977777773", "ana@email.com", "senha123", null);
        artesao3.setId(103L); // <-- ADICIONE ESTA LINHA
        Loja loja3 = new Loja("Prata da Casa", "Jóias artesanais em prata", null, artesao3);
        artesao3.setLoja(loja3); // Garante a relação bidirecional
        Produto produto3 = new Produto("Colar de Prata", "Colar com pingente de lua", new BigDecimal("250.00"), 5, loja3);
        produto3.setId(3L);
        produto3.setImagemPrincipalUrl("/images/colar-prata.jpg");
        produto3.getCategorias().add(catJoias);

        // Produto 4 Loja 1
        Produto produto4 = new Produto("Conjunto de Banheiro", "Guarda sabão, escova e cotonete", new BigDecimal("45.00"), 8, loja1);
        produto4.setId(4L);
        produto4.setImagemPrincipalUrl("/images/conjunto-banheiro.jpg");
        produto4.getCategorias().add(catCeramica);

        // Produto 5 Loja 2
        Produto produto5 = new Produto("Toalhas de Algodão", "Toalhas de banho e rosto", new BigDecimal("60.00"), 12, loja2);
        produto5.setId(5L);
        produto5.setImagemPrincipalUrl("/images/toalhas.jpg");
        produto5.getCategorias().add(catTexteis);
        

        produtosEmMemoria.add(produto1);
        produtosEmMemoria.add(produto2);
        produtosEmMemoria.add(produto3);
        produtosEmMemoria.add(produto4);
        produtosEmMemoria.add(produto5);
    }

    /**
    * Cadastra um novo produto para um artesão específico.
    */
    public void cadastrarProduto(ProdutoCadastroDTO dados, Artesao artesaoLogado) {
        // Busca a loja do artesão logado
        Loja lojaDoArtesao = artesaoLogado.getLoja();

        // Cria a nova entidade Produto
        Produto novoProduto = new Produto(
            dados.nome(),
            dados.descricao(),
            dados.preco(),
            dados.estoque(),
            lojaDoArtesao // Associa o produto à loja correta
        );
        novoProduto.setId(new AtomicLong(produtosEmMemoria.size() + 1).getAndIncrement()); // Simula ID
        novoProduto.setImagemPrincipalUrl(dados.imagemUrl());
    
        // Simulação de busca de categoria pelo ID
        // Em um sistema real, teríamos um CategoriaService para buscar a categoria.
        if (dados.categoriaId() != null) {
            Categoria cat = new Categoria();
            cat.setId(dados.categoriaId());
            // Apenas para exemplo, não temos o nome aqui
            if (dados.categoriaId() == 1L) cat.setNome("Cerâmica"); 
           if (dados.categoriaId() == 2L) cat.setNome("Têxteis");
            if (dados.categoriaId() == 3L) cat.setNome("Jóias");
        
            novoProduto.getCategorias().add(cat);
        }
    
        // Adiciona o novo produto à lista em memória
        produtosEmMemoria.add(novoProduto);
        System.out.println("Novo produto cadastrado: " + novoProduto.getNome() + " para a loja " + lojaDoArtesao.getNome());
    }

    /**
     * Retorna os 3 primeiros produtos para a home page.
     */
    public List<ProdutoDTO> listarProdutosEmDestaque() {
        return produtosEmMemoria.stream()
                .limit(3) // Pega apenas os 3 primeiros
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retorna TODOS os produtos, com filtro opcional por categoria.
     * @param categoriaSlug O nome da categoria em minúsculas (ex: "ceramica")
     */
    public List<ProdutoDTO> buscarTodos(Optional<String> categoriaSlug) {
        return produtosEmMemoria.stream()
                .filter(produto -> {
                    // Se o parâmetro 'categoriaSlug' não foi passado, o produto passa pelo filtro.
                    if (categoriaSlug.isEmpty()) {
                        return true;
                    }
                    // Se foi passado, verifica se alguma categoria do produto corresponde ao slug.
                    return produto.getCategorias().stream()
                            .anyMatch(cat -> cat.getNome().equalsIgnoreCase(categoriaSlug.get()));
                })
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

    /**
     * Busca um único produto pelo seu ID e o converte para DTO de detalhes.
     * @param id O ID do produto a ser buscado.
     * @return um Optional contendo o DTO se o produto for encontrado, ou um Optional vazio caso contrário.
     */
    public Optional<ProdutoDetalheDTO> buscarPorId(Long id) {
        return produtosEmMemoria.stream()
                .filter(produto -> produto.getId().equals(id)) // Encontra o produto com o ID correspondente
                .findFirst() // Pega o primeiro (e único) resultado
                .map(this::converterParaDetalheDTO); // Converte o resultado para DTO, se existir
    }

    /**
     * Método auxiliar para converter uma Entity Produto em um ProdutoDetalheDTO.
     */
    private ProdutoDetalheDTO converterParaDetalheDTO(Produto produto) {
        // Mapeia o Set<Categoria> para um Set<String> com os nomes das categorias
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

    /**
    * Busca todos os produtos pertencentes a um artesão (pelo ID do artesão).
    */
    public List<ProdutoDTO> buscarPorArtesaoId(Long artesaoId) {
        return produtosEmMemoria.stream()
            // Filtra os produtos cuja loja pertence ao artesão com o ID informado
            .filter(produto -> produto.getLoja().getArtesao().getId().equals(artesaoId))
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
}