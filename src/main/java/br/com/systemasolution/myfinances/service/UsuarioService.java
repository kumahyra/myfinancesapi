package br.com.systemasolution.myfinances.service;

import br.com.systemasolution.myfinances.model.entity.Usuario;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

    Optional<Usuario> obterPorId(Long id);

}
