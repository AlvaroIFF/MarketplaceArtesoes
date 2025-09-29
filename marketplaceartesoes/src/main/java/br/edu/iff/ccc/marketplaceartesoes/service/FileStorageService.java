package br.edu.iff.ccc.marketplaceartesoes.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final String UPLOAD_DIR = "images"; 

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads/" + UPLOAD_DIR)
                .toAbsolutePath().normalize();

        try {
            // Garante que o diretório exista
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Não foi possível criar o diretório para armazenar os arquivos.", ex);
        }
    }

    /**
     * Salva um arquivo na pasta de imagens e retorna o caminho de acesso via URL.
     * @param file O arquivo a ser salvo.
     * @return O caminho da URL para acessar a imagem (ex: /images/arquivo.jpg), ou null se o arquivo for nulo ou vazio.
     */
    public String salvarImagem(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilenameRaw = file.getOriginalFilename();
        if (originalFilenameRaw == null) {
            throw new RuntimeException("O arquivo enviado não possui um nome válido.");
        }
        String originalFilename = StringUtils.cleanPath(originalFilenameRaw);

        // Extrai a extensão do arquivo
        String extension = "";
        int i = originalFilename.lastIndexOf('.');
        if (i > 0) {
            extension = originalFilename.substring(i);
        }

        // Cria um nome de arquivo único para evitar colisões
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        try {
            // Verifica se o nome do arquivo contém caracteres inválidos
            if (uniqueFilename.contains("..")) {
                throw new RuntimeException("Nome de arquivo inválido: " + uniqueFilename);
            }

            // Define o caminho completo onde o arquivo será salvo no disco
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);

            // Copia o conteúdo do arquivo para o local de destino
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Retorna o caminho que será usado no HTML (acessível pela web)
            return "/" + UPLOAD_DIR + "/" + uniqueFilename;

        } catch (IOException ex) {
            throw new RuntimeException("Não foi possível armazenar o arquivo " + uniqueFilename, ex);
        }
    }

    // Você pode adicionar um método para deletar arquivos aqui se precisar no futuro
    public void deletarArquivo(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            return;
        }
        try {
            // Constrói o caminho do arquivo no sistema de arquivos a partir do caminho da URL
            Path pathCompleto = Paths.get("src/main/resources/static" + filePath).toAbsolutePath();
            Files.deleteIfExists(pathCompleto);
        } catch (IOException e) {
            System.err.println("Erro ao deletar o arquivo: " + filePath + " - " + e.getMessage());
        }
    }
}