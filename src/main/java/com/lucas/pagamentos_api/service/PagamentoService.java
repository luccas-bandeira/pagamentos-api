package com.lucas.pagamentos_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lucas.pagamentos_api.repository.PagamentoRepository;
import com.lucas.pagamentos_api.models.Pagamento;
import java.util.List;
import java.util.Objects;

@Service
public class PagamentoService {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @SuppressWarnings("unlikely-arg-type")
    public List<Pagamento> filtrarPagamentos(Integer codigoDebito, String identificadorPagador, String status,
            Integer id) {
        return pagamentoRepository.findAll().stream()
                .filter(p -> codigoDebito == null || Objects.equals(p.getCodigoDebito(), codigoDebito))
                .filter(p -> identificadorPagador == null || Objects.equals(p.getIdentificadorPagador(), identificadorPagador))
                .filter(p -> status == null || Objects.equals(p.getStatus(), status))
                .filter(p -> id == null || Objects.equals(p.getId(), id))
                .toList();
    }
}
