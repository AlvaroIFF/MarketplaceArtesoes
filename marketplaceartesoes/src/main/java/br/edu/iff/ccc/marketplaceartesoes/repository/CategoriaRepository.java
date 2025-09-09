package br.edu.iff.ccc.marketplaceartesoes.repository;

import br.edu.iff.ccc.marketplaceartesoes.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}