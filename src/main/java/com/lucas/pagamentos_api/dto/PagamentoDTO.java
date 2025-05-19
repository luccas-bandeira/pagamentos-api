package com.lucas.pagamentos_api.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class PagamentoDTO {

    @NotNull(message = "codigoDebito é obrigatório")
    private Integer codigoDebito;

    @NotBlank(message = "identificadorPagador é obrigatório")
    private String identificadorPagador;

    @NotBlank(message = "metodoPagamento é obrigatório")
    private String metodoPagamento;

    private String numeroCartao;

    @NotNull(message = "valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "valor deve ser positivo")
    private Double valor;

    @NotBlank(message = "status é obrigatório")
    private String status;

    @NotNull(message = "ativo é obrigatório")
    private Boolean ativo;

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public Integer getCodigoDebito() {
        return codigoDebito;
    }

    public void setCodigoDebito(Integer codigoDebito) {
        this.codigoDebito = codigoDebito;
    }

    public String getIdentificadorPagador() {
        return identificadorPagador;
    }

    public void setIdentificadorPagador(String identificadorPagador) {
        this.identificadorPagador = identificadorPagador;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public static final List<String> METODOS_VALIDOS = List.of("boleto", "pix", "cartao_credito", "cartao_debito");
}
