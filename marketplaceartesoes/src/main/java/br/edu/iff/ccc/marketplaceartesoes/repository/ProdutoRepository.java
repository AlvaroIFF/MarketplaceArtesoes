package br.edu.iff.ccc.marketplaceartesoes.repository;

import br.edu.iff.ccc.marketplaceartesoes.entities.Loja;
import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.loja.artesao.id = :artesaoId")
    List<Produto> findByArtesaoId(@Param("artesaoId") Long artesaoId);

    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE LOWER(c.nome) = LOWER(:nomeCategoria)")
    List<Produto> findByCategoriaNome(@Param("nomeCategoria") String nomeCategoria);

    @Query("SELECT p FROM Produto p ORDER BY p.id ASC")
    List<Produto> findProdutosEmDestaque(Pageable pageable);

    List<Produto> findByLoja(Loja loja); 

    List<Produto> findByLojaId(Long lojaId); 
}