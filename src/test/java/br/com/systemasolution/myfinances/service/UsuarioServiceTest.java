package br.com.systemasolution.myfinances.service;

import br.com.systemasolution.myfinances.exception.ErroAutenticacao;
import br.com.systemasolution.myfinances.exception.RegraNegocioException;
import br.com.systemasolution.myfinances.model.entity.Usuario;
import br.com.systemasolution.myfinances.model.repository.UsuarioRepository;
import br.com.systemasolution.myfinances.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl usuarioService;

    @MockBean
    UsuarioRepository usuarioRepository;

    @Test
    public void deveSalvarUmUsuarioComSucesso(){
        // cenario
        Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("nome")
                .email("email@email.com")
                .senha("senha").build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        // acao ou execucao
        Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());

        // verificacao
        Assertions.assertDoesNotThrow(() -> usuarioSalvo);
        Assertions.assertEquals(1L, usuario.getId());
        Assertions.assertEquals("nome", usuario.getNome());
        Assertions.assertEquals("email@email.com", usuario.getEmail());
        Assertions.assertEquals("senha", usuario.getSenha());

    }

    @Test
    public void naoDeveSalvarComEmailJaCadastrado(){
        // Cenario
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);

        // acao ou execucao
        Assertions.assertThrows(RegraNegocioException.class,
                () -> usuarioService.salvarUsuario(usuario));

        // verificacao

        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso(){
        // cenario
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // acao ou execucao

        // verificacao
        Assertions.assertDoesNotThrow(() -> usuarioService.autenticar(email, senha));

    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado(){
        // cenario
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        // acao
        Exception exception = Assertions.assertThrows(ErroAutenticacao.class,
                () -> usuarioService.autenticar("email@email.com", "senha"));

        // verificacao
        String expectedMessage = "Usuário não encontrado para o email informado!";
        String acttualMessage = exception.getMessage();

        Assertions.assertTrue(acttualMessage.equals(expectedMessage));
    }

    @Test
    public void deveLancarErroComSenhaInvalida(){
        // cenario
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        // acao ou execucao
        Exception exception = Assertions.assertThrows(ErroAutenticacao.class,
                () -> usuarioService.autenticar("email@email.com", "123456"));

        // verificacao

        String expectedMessage = "Senha inválida!";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void deveValidarEmail(){
        // cenario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        // acao / execucao

         Assertions.assertDoesNotThrow(() -> usuarioService.validarEmail("email@email.com"));

        // verificacao
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        // cenario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        // acao / execucao
        Exception exception = Assertions.assertThrows(RegraNegocioException.class,
                () -> usuarioService.validarEmail("email@email.com"));

        // verificacao

        String expectedMessage = "Já Existe um usuário cadastrado com este email!";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
