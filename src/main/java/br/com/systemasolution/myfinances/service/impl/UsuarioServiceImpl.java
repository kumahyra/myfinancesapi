package br.com.systemasolution.myfinances.service.impl;

import br.com.systemasolution.myfinances.exception.ErroAutenticacao;
import br.com.systemasolution.myfinances.exception.RegraNegocioException;
import br.com.systemasolution.myfinances.model.entity.Usuario;
import br.com.systemasolution.myfinances.model.repository.UsuarioRepository;
import br.com.systemasolution.myfinances.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        super();
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioServiceImpl() {
        super();
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if(!usuario.isPresent()){
            throw new ErroAutenticacao("Usuário não encontrado para o email informado!");
        }

        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha inválida!");
        }

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        if(usuarioRepository.existsByEmail(email)){
            throw new RegraNegocioException("Já Existe um usuário cadastrado com este email!");
        }
    }

    @Override
    public Optional<Usuario> obterPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
