package br.edu.iff.ccc.marketplaceartesoes.repository;

import br.edu.iff.ccc.marketplaceartesoes.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.dtPedido DESC")
    List<Pedido> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("SELECT p FROM Pedido p JOIN p.itens i JOIN i.produto prod WHERE prod.loja.artesao.id = :artesaoId")
    List<Pedido> findPedidosByArtesaoId(@Param("artesaoId") Long artesaoId);

}