package br.com.systemasolution.myfinances.model.repository;

import br.com.systemasolution.myfinances.model.entity.Lancamento;
import br.com.systemasolution.myfinances.shared.enums.StatusLancamento;
import br.com.systemasolution.myfinances.shared.enums.TipoLancamento;
import org.junit.jupiter.api.Assertions;
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
        Assertions.assertTrue(lancamentos.getId() != null);
    }

    @Test
    public void deveDeletarUmLancamento(){
        // cenario
        Lancamento lancamento = criarUmLancamentoTest();
        entityManager.persist(lancamento);
        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        // acao ou execucao
        repository.delete(lancamento);

        // verificacao
        Lancamento lancamentoDeletado = entityManager.find(Lancamento.class, lancamento.getId());
        Assertions.assertTrue(lancamentoDeletado == null);
    }

    private Lancamento criarUmLancamentoTest() {
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
}
