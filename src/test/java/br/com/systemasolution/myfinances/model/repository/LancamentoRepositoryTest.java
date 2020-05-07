package br.com.systemasolution.myfinances.model.repository;

import br.com.systemasolution.myfinances.model.entity.Lancamento;
import br.com.systemasolution.myfinances.shared.enums.StatusLancamento;
import br.com.systemasolution.myfinances.shared.enums.TipoLancamento;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento(){
        // cenario
        Lancamento lancamentos = criarUmLancamentoTest();

        // acao ou execucao
        lancamentos = repository.save(lancamentos);

        // verificacao
        assertTrue(lancamentos.getId() != null);
    }

    @Test
    public void deveDeletarUmLancamento(){
        // cenario
        Lancamento lancamento = persistirUmLancamento();

        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        // acao ou execucao
        repository.delete(lancamento);

        // verificacao
        Lancamento lancamentoDeletado = entityManager.find(Lancamento.class, lancamento.getId());
        assertTrue(lancamentoDeletado == null);
    }

    @Test
    public void deveAtualizarUmLancamento(){

        // cenario
        Lancamento lancamento = persistirUmLancamento();

        // acao ou execucao
        lancamento.setAno(2018);
        lancamento.setDescricao("Atualizar teste");
        lancamento.setStatus(StatusLancamento.CANCELADO);
        repository.save(lancamento);

        // verificacao
        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        assertEquals(2018, lancamento.getAno());
        assertEquals("Atualizar teste", lancamento.getDescricao());
        assertEquals(StatusLancamento.CANCELADO, lancamento.getStatus());

    }

    @Test
    public void deveBuscarUmLancamentoPorId(){
        // cenario
        Lancamento lancamento = persistirUmLancamento();

        // acao ou execucao
        Optional<Lancamento> lancamentoEcontrado = repository.findById(lancamento.getId());

        // verificacao
        assertTrue(lancamentoEcontrado.isPresent());
    }

    public static Lancamento criarUmLancamentoTest() {
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("Lançamento teste")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.DESPESA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }

    private Lancamento persistirUmLancamento() {
        Lancamento lancamento = criarUmLancamentoTest();
        entityManager.persist(lancamento);
        return lancamento;
    }
}
