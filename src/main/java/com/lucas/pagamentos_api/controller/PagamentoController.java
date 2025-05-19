package com.lucas.pagamentos_api.controller;

import com.lucas.pagamentos_api.dto.PagamentoDTO;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import com.lucas.pagamentos_api.models.Pagamento;
import com.lucas.pagamentos_api.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository repository;

    @GetMapping
    public List<Pagamento> listarPagamentos() {
        return repository.findAll();
    }

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @PostMapping
    public ResponseEntity<?> criarPagamentos(@RequestBody @Valid List<PagamentoDTO> pagamentos, BindingResult result) {
        if (result.hasErrors()) {
            // Validação do DTO
            String errorMessage = result.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
        }

        for (PagamentoDTO p : pagamentos) {
            if (!PagamentoDTO.METODOS_VALIDOS.contains(p.getMetodoPagamento())) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Método de pagamento inválido: " + p.getMetodoPagamento() +
                                ". Válidos: " + String.join(", ", PagamentoDTO.METODOS_VALIDOS)));
            }

            if ((p.getMetodoPagamento().equals("cartao_credito") || p.getMetodoPagamento().equals("cartao_debito"))
                    && (p.getNumeroCartao() == null || p.getNumeroCartao().isBlank())) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Número do cartão é obrigatório para pagamentos com cartão."));
            }

            if ((p.getMetodoPagamento().equals("boleto") || p.getMetodoPagamento().equals("pix"))
                    && p.getNumeroCartao() != null && !p.getNumeroCartao().isBlank()) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "Número do cartão não deve ser informado para boleto ou pix."));
            }
        }

        List<Pagamento> entidades = pagamentos.stream().map(dto -> {
            Pagamento p = new Pagamento();
            p.setCodigoDebito(dto.getCodigoDebito());
            p.setIdentificadorPagador(dto.getIdentificadorPagador());
            p.setMetodoPagamento(dto.getMetodoPagamento());
            p.setNumeroCartao(dto.getNumeroCartao());
            p.setValor(dto.getValor());
            p.setStatus(dto.getStatus());
            p.setAtivo(dto.getAtivo());
            return p;
        }).toList();

        List<Pagamento> salvos = pagamentoRepository.saveAll(entidades);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvos);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Status é obrigatório."));
        }

        return repository.findById(id)
                .map(p -> {
                    if ("PROCESSADO_SUCESSO".equals(p.getStatus())) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error",
                                        "Não é possível alterar um pagamento já processado com sucesso."));
                    }

                    if ("PROCESSADO_FALHA".equals(p.getStatus()) && !"PENDENTE_PROCESSAMENTO".equals(status)) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "Somente pode voltar para PENDENTE_PROCESSAMENTO após falha."));
                    }

                    if ("PENDENTE_PROCESSAMENTO".equals(p.getStatus())
                            && !List.of("PROCESSADO_SUCESSO", "PROCESSADO_FALHA").contains(status)) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "Status inválido para atualização."));
                    }

                    p.setStatus(status);
                    Pagamento atualizado = repository.save(p);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Pagamento não encontrado.")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativarPagamento(@PathVariable Long id) {
        return repository.findById(id)
                .map(p -> {
                    if (!Boolean.TRUE.equals(p.getAtivo())) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Pagamento já inativo."));
                    }

                    if (!"PENDENTE_PROCESSAMENTO".equals(p.getStatus())) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error",
                                        "Apenas pagamentos pendentes de processamento podem ser inativados."));
                    }

                    p.setAtivo(false);
                    repository.save(p);
                    return ResponseEntity.ok(Map.of("message", "Pagamento inativado com sucesso."));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Pagamento não encontrado.")));
    }
}
