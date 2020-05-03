package br.com.systemasolution.myfinances.service;

import br.com.systemasolution.myfinances.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

}
