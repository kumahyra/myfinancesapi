package br.com.systemasolution.myfinances.model.repository;

import br.com.systemasolution.myfinances.model.entity.Lancamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamentos, Long> {
}
