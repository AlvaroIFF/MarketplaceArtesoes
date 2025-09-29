package br.edu.iff.ccc.marketplaceartesoes.controller.restapi;

import br.edu.iff.ccc.marketplaceartesoes.dto.ApiStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1")
public class RestApiMainController {

    @GetMapping
    public ResponseEntity<ApiStatusDTO> getApiHome() {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        Map<String, String> links = Map.of(
            "clientes", baseUrl + "/api/v1/clientes",
            "artesaos", baseUrl + "/api/v1/artesaos",
            "lojas", baseUrl + "/api/v1/lojas",
            "categorias", baseUrl + "/api/v1/categorias",
            "produtos", baseUrl + "/api/v1/produtos",
            "pedidos", baseUrl + "/api/v1/pedidos",
            "documentacao_swagger", baseUrl + "/swagger-ui.html"
        );
        
        ApiStatusDTO status = new ApiStatusDTO(
            "API do Marketplace de Artesãos está online!",
            "1.0.0",
            Instant.now(),
            links
        );
        
        return ResponseEntity.ok(status);
    }
}