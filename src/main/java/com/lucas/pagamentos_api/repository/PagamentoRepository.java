package com.lucas.pagamentos_api.repository;

import com.lucas.pagamentos_api.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
