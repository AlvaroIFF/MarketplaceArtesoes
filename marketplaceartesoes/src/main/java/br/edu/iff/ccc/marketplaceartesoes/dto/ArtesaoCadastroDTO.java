package br.edu.iff.ccc.marketplaceartesoes.dto;

import java.time.LocalDate;

// Este 'record' agrupa todos os campos do formulário de cadastro do artesão.
public record ArtesaoCadastroDTO(
    // Dados Pessoais do Artesão (herdados de Usuario)
    String nome,
    String cpf,
    LocalDate dtNasc,
    String numContato,
    String email,
    String senha,
    String confirmarSenha,

    // Dados da Loja
    String nomeLoja,
    String descricaoLoja,
    String cnpj // O CNPJ é opcional
) {}