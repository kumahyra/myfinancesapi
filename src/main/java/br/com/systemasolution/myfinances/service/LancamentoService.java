package br.com.systemasolution.myfinances.service;

import br.com.systemasolution.myfinances.model.entity.Lancamento;
import br.com.systemasolution.myfinances.shared.enums.StatusLancamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvar(Lancamento lancamentos);

    Lancamento atualizar(Lancamento lancamentos);

    void deletar(Lancamento lancamentos);

    List<Lancamento> buscar(Lancamento lancamentosFiltros);

    void atualizarSatus(Lancamento lancamentos, StatusLancamento statusLancamento);

    void validar(Lancamento lancamentos);

    Optional<Lancamento> obterPorId(Long id);

    BigDecimal obterSaldoPorUsuario(Long id);

}
