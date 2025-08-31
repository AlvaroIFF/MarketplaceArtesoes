package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.time.LocalDate;

// Usando record para simplicidade. Ele representa os dados que vêm do formulário.
public record ClienteCadastroDTO(
    String nome,
    String cpf,
    LocalDate dtNasc,
    String numContato,
    String email,
    String senha,
    String confirmarSenha
) {}