package br.com.systemasolution.myfinances.api.resource;

import br.com.systemasolution.myfinances.api.dto.UsuarioDTO;
import br.com.systemasolution.myfinances.exception.ErroAutenticacao;
import br.com.systemasolution.myfinances.exception.RegraNegocioException;
import br.com.systemasolution.myfinances.model.entity.Usuario;
import br.com.systemasolution.myfinances.service.LancamentoService;
import br.com.systemasolution.myfinances.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc
public class UsuarioResourceTest {

    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioService service;

    @MockBean
    LancamentoService lancamentoService;

    @MockBean
    ModelMapper modelMapper;

    @Test
    public void deveAutenticarUmUsuario() throws Exception {
        // cenario
        String email = "usuario@email.com";
        String senha = "senha";

        UsuarioDTO dto = UsuarioDTO.builder()
                .email(email)
                .senha(senha)
                .build();

        Usuario usuario = Usuario.builder()
                .id(1L)
                .email(email)
                .senha(senha)
                .build();

        Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        // acao ou execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);


        // veriricacao
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }

    @Test
    public void deveRetornarUmBadRequestAoObterErroDeAutenticacao() throws Exception {
        // cenario
        String email = "usuario@email.com";
        String senha = "senha";

        UsuarioDTO dto = UsuarioDTO.builder()
                .email(email)
                .senha(senha)
                .build();

        Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);

        String json = new ObjectMapper().writeValueAsString(dto);

        // acao ou execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);


        // veriricacao
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    /*@Test
    public void deveSalvarUmNovoUsuario() throws Exception {
        // cenario
        Long id = 1L;
        String email = "usuario@email.com";
        String senha = "123456";
        String nome = "Kumahyra";

        UsuarioDTO dto = UsuarioDTO.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .build();

        Usuario usuario = Usuario.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .senha(senha)
                .build();

        Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = new ObjectMapper().writeValueAsString(dto);

        // acao ou execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .accept(JSON)
                .contentType(JSON)
                .content(json);


        // veriricacao
        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("id"))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }*/

    @Test
    public void deveCriarUmNovoUsuario() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();
        Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();

        //Mockito.doNothing().when(modelMapper).map(dto, Usuario.class);

        Mockito.when( service.salvarUsuario(Mockito.any(Usuario.class)) ).thenReturn(usuario);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API  )
                .accept( JSON )
                .contentType( JSON )
                .content(json);
        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isCreated())
                .andExpect( MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                .andExpect( MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                .andExpect( MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));

    }

    @Test
    public void deveRetornarUmBadRequestAoSalvarUmUsuarioInvalido() throws Exception {
        //cenario
        String email = "usuario@email.com";
        String senha = "123";

        UsuarioDTO dto = UsuarioDTO.builder().email("usuario@email.com").senha("123").build();

        Mockito.when( service.salvarUsuario(Mockito.any(Usuario.class)) ).thenThrow(RegraNegocioException.class);
        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( API  )
                .accept( JSON )
                .contentType( JSON )
                .content(json);
        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isBadRequest());

    }
}
