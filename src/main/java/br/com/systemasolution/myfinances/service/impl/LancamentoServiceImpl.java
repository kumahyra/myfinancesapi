package br.com.systemasolution.myfinances.service.impl;

import br.com.systemasolution.myfinances.exception.RegraNegocioException;
import br.com.systemasolution.myfinances.model.entity.Lancamentos;
import br.com.systemasolution.myfinances.shared.enums.StatusLancamento;
import br.com.systemasolution.myfinances.model.repository.LancamentoRepository;
import br.com.systemasolution.myfinances.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService{

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Override
    @Transactional
    public Lancamentos salvar(Lancamentos lancamentos) {
        lancamentos.setStatus(StatusLancamento.PENDENTE);
        validar(lancamentos);
        return lancamentoRepository.save(lancamentos);
    }

    @Override
    @Transactional
    public Lancamentos atualizar(Lancamentos lancamentos) {
        Objects.requireNonNull(lancamentos.getId());
        validar(lancamentos);
        return lancamentoRepository.save(lancamentos);
    }

    @Override
    @Transactional
    public void deletar(Lancamentos lancamentos) {
        Objects.requireNonNull(lancamentos.getId());
        lancamentoRepository.delete(lancamentos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lancamentos> buscar(Lancamentos lancamentosFiltros) {
        Example example = Example.of(lancamentosFiltros,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return lancamentoRepository.findAll(example);
    }

    @Override
    public void atualizarSatus(Lancamentos lancamentos, StatusLancamento statusLancamento) {
        lancamentos.setStatus(statusLancamento);
        atualizar(lancamentos);
    }

    @Override
    public void validar(Lancamentos lancamentos) {
        if(lancamentos.getDescricao() == null || lancamentos.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Informe uma descrição válida!");
        }

        if(lancamentos.getMes() == null || lancamentos.getMes() < 1 || lancamentos.getMes() > 12){
            throw new RegraNegocioException("Informe um mês válido!");
        }

        if(lancamentos.getAno() == null || lancamentos.getAno().toString().length() != 4){
            throw new RegraNegocioException("Informa um ano válido!");
        }

        if(lancamentos.getUsuario() == null || lancamentos.getUsuario().getId() == null){
            throw new RegraNegocioException("Informe um usuário válido!");
        }

        if(lancamentos.getValor() == null || lancamentos.getValor().compareTo(BigDecimal.ZERO) < 1){
            throw new RegraNegocioException("Informe um valor válido!");
        }

        if(lancamentos.getTipo() == null){
            throw new RegraNegocioException("Informe um tipo de lançamento!");
        }

    }

    @Override
    public Optional<Lancamentos> obterPorId(Long id) {
        return lancamentoRepository.findById(id);
    }
}
