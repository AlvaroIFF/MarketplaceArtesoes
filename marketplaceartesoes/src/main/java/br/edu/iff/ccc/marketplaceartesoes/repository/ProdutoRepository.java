package br.edu.iff.ccc.marketplaceartesoes.repository;

import br.edu.iff.ccc.marketplaceartesoes.entities.Produto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    //Busca produtos pela loja de um artesão
    @Query("SELECT p FROM Produto p WHERE p.loja.artesao.id = :artesaoId")
    List<Produto> findByArtesaoId(@Param("artesaoId") Long artesaoId);
    //Busca produtos que pertencem a uma categoria
    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE LOWER(c.nome) = LOWER(:nomeCategoria)")
    List<Produto> findByCategoriaNome(@Param("nomeCategoria") String nomeCategoria);
    //Busca os produtos em destaque 
    @Query("SELECT p FROM Produto p ORDER BY p.id ASC")
    List<Produto> findProdutosEmDestaque(Pageable pageable);
}