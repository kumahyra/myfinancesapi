package br.com.systemasolution.myfinances.model.repository;

import br.com.systemasolution.myfinances.model.entity.Usuario;
import jdk.nashorn.internal.runtime.options.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenario
        Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
        testEntityManager.persist(usuario);

        //acao / execucao
        boolean existeEmail = usuarioRepository.existsByEmail("usuario@email.com");

        //verificacao
        Assertions.assertThat(existeEmail).isTrue();

    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail(){
        // cenario

        // acao / execucao
        boolean existeEmail = usuarioRepository.existsByEmail("usuario@email.com");

        // verificacao

        Assertions.assertThat(existeEmail).isFalse();
    }

    @Test
    public void devePersistirUmUsuarioNaBaseDeDados(){
        //cenario
        Usuario usuario = criarUsuario();

        //acao / execucao
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        //verificacao
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();

    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        // cenario
        Usuario usuario = criarUsuario();

        // acao / execucao
        testEntityManager.persist(usuario);

        // verificacao
        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
        Assertions.assertThat(result.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioaoBuscarUsuarioPorEmailNaBase(){
        // cenario

        // acao / execucao

        // verificacao
        Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
        Assertions.assertThat(result.isPresent()).isFalse();
    }

    public static Usuario criarUsuario(){
        return
                Usuario
                .builder()
                .nome("usuario")
                .email("usuario@email.com")
                .senha("senha")
                .build();
    }

}
