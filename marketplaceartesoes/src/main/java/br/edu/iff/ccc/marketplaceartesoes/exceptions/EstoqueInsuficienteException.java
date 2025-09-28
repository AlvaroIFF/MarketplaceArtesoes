package br.edu.iff.ccc.marketplaceartesoes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String nomeProduto, int quantidadeDisponivel) {
        super("Estoque insuficiente para o produto '" + nomeProduto + "'. Apenas " + quantidadeDisponivel + " unidades disponíveis.");
    }
}