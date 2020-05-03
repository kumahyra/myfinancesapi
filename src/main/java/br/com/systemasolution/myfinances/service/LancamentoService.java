package br.com.systemasolution.myfinances.service;

import br.com.systemasolution.myfinances.model.entity.Lancamentos;
import br.com.systemasolution.myfinances.model.entity.StatusLancamento;

import java.util.List;

public interface LancamentoService {

    Lancamentos salvar(Lancamentos lancamentos);

    Lancamentos atualizar(Lancamentos lancamentos);

    void deletar(Lancamentos lancamentos);

    List<Lancamentos> buscar(Lancamentos lancamentosFiltros);

    void atualizarSatus(Lancamentos lancamentos, StatusLancamento statusLancamento);

    void validar(Lancamentos lancamentos);

}
