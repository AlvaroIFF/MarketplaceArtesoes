package br.edu.iff.ccc.marketplaceartesoes.dto;

import br.edu.iff.ccc.marketplaceartesoes.entities.Cliente;
import java.time.LocalDate;

public record ClienteUpdateDTO(
        String nome,
        String cpf,
        LocalDate dtNasc,
        String numContato,
        String email
) {
    public ClienteUpdateDTO(Cliente cliente) {
        this(
            cliente.getNome(),
            cliente.getCpf(),
            cliente.getDtNasc(),
            cliente.getNumContato(),
            cliente.getEmail()
        );
    }
}