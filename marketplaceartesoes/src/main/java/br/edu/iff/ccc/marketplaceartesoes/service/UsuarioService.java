package br.edu.iff.ccc.marketplaceartesoes.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.edu.iff.ccc.marketplaceartesoes.entities.Usuario;

@Service
public class UsuarioService {
    
    public void saveUsuario(Usuario user) {
        System.out.println("ID: " + user.getId() 
        + "Nome: " + user.getNome() 
        + "CPF: " + user.getCpf() 
        + "Data de Nascimento: " + user.getDtNasc() 
        + "Celular: " + user.getNumContato() 
        + "E-mail: " + user.getEmail());
    }
    
    public Usuario findByID(Long id) {
        Usuario user = new Usuario();

        if (id==null) {
            return null;
        }

        user.setId(id);
        user.setNome("Teste da Silva");
        user.setCpf("999.999.999-99");
        user.setDtNasc(new Date(01/01/2001));
        user.setNumContato("22997653220");
        user.setEmail("teste.da.silva@dominio.com");

        return user;
    }
}
