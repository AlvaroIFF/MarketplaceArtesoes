package br.edu.iff.ccc.marketplaceartesoes.service;

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
        
        // Artesão 1
        Artesao artesao1 = new Artesao("Maria Silva", "11111111111", LocalDate.now().minusYears(30), "21999999991", "maria@email.com", "senha123", null);
        Loja loja1 = new Loja("Ateliê da Maria", "Cerâmicas artesanais", null, artesao1);
        Produto produto1 = new Produto("Vaso de Cerâmica", "Vaso feito à mão", new BigDecimal("120.00"), 10, loja1);
        produto1.setId(1L);
        produto1.setImagemPrincipalUrl("/images/vaso-ceramica.jpg");
        produto1.getCategorias().add(catCeramica); // Associando categoria

        // Artesão 2
        Artesao artesao2 = new Artesao("João Costa", "22222222222", LocalDate.now().minusYears(45), "11988888882", "joao@email.com", "senha123", null);
        Loja loja2 = new Loja("Fios e Nós", "Bolsas e acessórios em crochê", null, artesao2);
        Produto produto2 = new Produto("Bolsa de Crochê", "Bolsa de ombro colorida", new BigDecimal("85.50"), 15, loja2);
        produto2.setId(2L);
        produto2.setImagemPrincipalUrl("/images/bolsa-croche.jpg");
        produto2.getCategorias().add(catTexteis); // Associando categoria

        // Artesão 3
        Artesao artesao3 = new Artesao("Ana Pereira", "33333333333", LocalDate.now().minusYears(28), "81977777773", "ana@email.com", "senha123", null);
        Loja loja3 = new Loja("Prata da Casa", "Jóias artesanais em prata", null, artesao3);
        Produto produto3 = new Produto("Colar de Prata", "Colar com pingente de lua", new BigDecimal("250.00"), 5, loja3);
        produto3.setId(3L);
        produto3.setImagemPrincipalUrl("/images/colar-prata.jpg");
        produto3.getCategorias().add(catJoias); // Associando categoria

        produtosEmMemoria.add(produto1);
        produtosEmMemoria.add(produto2);
        produtosEmMemoria.add(produto3);
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
}