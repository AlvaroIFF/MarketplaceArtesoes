package br.edu.iff.ccc.marketplaceartesoes.entities;

public class Administrador extends Usuario{
    private String nivelAcesso;

    public Administrador() {}

    public Administrador(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }
}
