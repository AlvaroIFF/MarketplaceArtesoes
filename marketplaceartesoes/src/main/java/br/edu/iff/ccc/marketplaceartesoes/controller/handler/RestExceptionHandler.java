package br.edu.iff.ccc.marketplaceartesoes.controller.handler;

import br.edu.iff.ccc.marketplaceartesoes.exceptions.ArtesaoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.AutenticacaoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ClienteNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.EstoqueInsuficienteException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.LojaNaoEncontradaException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.PedidoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.ProdutoNaoEncontradoException;
import br.edu.iff.ccc.marketplaceartesoes.exceptions.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ProdutoNaoEncontradoException.class, ArtesaoNaoEncontradoException.class, ClienteNaoEncontradoException.class, LojaNaoEncontradaException.class, PedidoNaoEncontradoException.class})
    public ProblemDetail handleResourceNotFoundException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso Não Encontrado");
        problemDetail.setType(URI.create("https://api.seusite.com/errors/not-found")); // URL hipotética de documentação
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler({RegraDeNegocioException.class, EstoqueInsuficienteException.class})
    public ProblemDetail handleBusinessRuleException(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Violação de Regra de Negócio");
        problemDetail.setType(URI.create("https://api.seusite.com/errors/bad-request"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(AutenticacaoException.class)
    public ProblemDetail handleAutenticacaoException(AutenticacaoException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setTitle("Acesso Negado");
        problemDetail.setType(URI.create("https://api.seusite.com/errors/unauthorized"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}