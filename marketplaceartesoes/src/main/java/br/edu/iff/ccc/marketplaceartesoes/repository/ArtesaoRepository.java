package br.edu.iff.ccc.marketplaceartesoes.repository;

import br.edu.iff.ccc.marketplaceartesoes.entities.Artesao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtesaoRepository extends JpaRepository<Artesao, Long> {

    Optional<Artesao> findByEmail(String email);

    Optional<Artesao> findByCpf(String cpf);

    Optional<Artesao> findByEmailAndIdNot(String email, Long id);

    Optional<Artesao> findByCpfAndIdNot(String cpf, Long id);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

}