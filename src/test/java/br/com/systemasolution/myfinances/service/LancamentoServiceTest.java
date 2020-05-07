package br.com.systemasolution.myfinances.service;

import br.com.systemasolution.myfinances.exception.RegraNegocioException;
import br.com.systemasolution.myfinances.model.entity.Lancamento;
import br.com.systemasolution.myfinances.model.entity.Usuario;
import br.com.systemasolution.myfinances.model.repository.LancamentoRepository;
import br.com.systemasolution.myfinances.model.repository.LancamentoRepositoryTest;
import br.com.systemasolution.myfinances.service.impl.LancamentoServiceImpl;
import br.com.systemasolution.myfinances.shared.enums.StatusLancamento;
import br.com.systemasolution.myfinances.shared.enums.TipoLancamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImpl serivce;

    @MockBean
    LancamentoRepository repository;

    @Test
    public void deveSalvarUmLancamento(){
        // cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarUmLancamentoTest();
        Mockito.doNothing().when(serivce).validar(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        // acao ou execucao
        Lancamento lancamento = serivce.salvar(lancamentoASalvar);

        // verificacao
        Assertions.assertEquals(lancamentoSalvo.getId(), lancamento.getId());
        Assertions.assertEquals(lancamentoSalvo.getStatus(), StatusLancamento.PENDENTE);

    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){
        // cenario
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarUmLancamentoTest();
        Mockito.doThrow(RegraNegocioException.class).when(serivce).validar(lancamentoASalvar);

        // acao ou execucao
        Assertions.assertThrows(RegraNegocioException.class,
                () -> serivce.salvar(lancamentoASalvar));

        // verificacao
        Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
    }

    @Test
    public void deveAtualizarUmLancamento(){
        // cenario
        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.doNothing().when(serivce).validar(lancamentoSalvo);
        Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

        // acao ou execucao
        serivce.atualizar(lancamentoSalvo);

        // verificacao
        Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
    }

    @Test
    public void deveLancarErroAoTentarAtualizarUmLanamentoNaoSalvo(){
        // cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();

        // acao ou execucao
        Assertions.assertThrows(NullPointerException.class,
                () -> serivce.atualizar(lancamento));

        // verificacao
        Mockito.verify(repository, Mockito.never()).save(lancamento);
    }

    @Test
    public void deveDeletarUmLancamento(){
        // cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamento.setId(1L);

        // acao ou execucao
        serivce.deletar(lancamento);

        // verificao
        Mockito.verify(repository).delete(lancamento);
    }

    @Test
    public void deveLancarErroAoTentarDeletarUmLanamentoNaoSalvo(){
        // cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();

        // acao ou execucao
        Assertions.assertThrows(NullPointerException.class,
                () -> serivce.atualizar(lancamento));

        // verificao
        Mockito.verify(repository, Mockito.never()).delete(lancamento);
    }

    @Test
    public void deveFiltrarLancamento(){
        // cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamento.setId(1L);
        List<Lancamento> listaLancamento = Arrays.asList(lancamento);
        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(listaLancamento);

        // acao ou execucao
        List<Lancamento> resultadoLancamentos = serivce.buscar(lancamento);

        // verificacao
        Assertions.assertTrue(!resultadoLancamentos.isEmpty());
        Assertions.assertEquals(1, resultadoLancamentos.size());
        Assertions.assertTrue(resultadoLancamentos.contains(lancamento));
    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento(){
        // cenario
        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamento.setId(1L);
        lancamento.setStatus(StatusLancamento.PENDENTE);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamento).when(serivce).atualizar(lancamento);

        // acao ou execucao
        serivce.atualizarSatus(lancamento, novoStatus);

        // verificacao
        Assertions.assertEquals(novoStatus, lancamento.getStatus());
        Mockito.verify(serivce).atualizar(lancamento);

    }

    @Test
    public void deveObterUmLancamentoPorId(){
        // cenario
        Long id = 1L;

        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));

        // acao ou execucao
        Optional<Lancamento> resultado = serivce.obterPorId(id);

        // verificacao
        Assertions.assertTrue(resultado.isPresent());

    }

    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExistir(){
        // cenario
        Long id = 1L;

        Lancamento lancamento = LancamentoRepositoryTest.criarUmLancamentoTest();
        lancamento.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // acao ou execucao
        Optional<Lancamento> resultado = serivce.obterPorId(id);

        // verificacao
        Assertions.assertTrue(!resultado.isPresent());

    }

    @Test
    public void deveLancarErrosAoValidarLancamento(){
        Lancamento lancamento = new Lancamento();

        Exception exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe uma descrição válida!"));

        lancamento.setDescricao("");

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe uma descrição válida!"));
        lancamento.setDescricao("Descrição");

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um mês válido!"));

        lancamento.setMes(0);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um mês válido!"));

        lancamento.setMes(13);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um mês válido!"));

        lancamento.setMes(1);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um ano válido!"));

        lancamento.setAno(202);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um ano válido!"));

        lancamento.setAno(20201);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um ano válido!"));

        lancamento.setAno(2020);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um usuário válido!"));

        lancamento.setUsuario(new Usuario());

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um usuário válido!"));

        lancamento.getUsuario().setId(1L);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um valor válido!"));

        lancamento.setValor(BigDecimal.ZERO);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um valor válido!"));

        lancamento.setValor(BigDecimal.ONE);

        exception = Assertions.assertThrows(RegraNegocioException.class, () -> serivce.validar(lancamento));
        Assertions.assertTrue(exception.getMessage().contains("Informe um tipo de lançamento!"));
        lancamento.setTipo(TipoLancamento.DESPESA);

    }
}
