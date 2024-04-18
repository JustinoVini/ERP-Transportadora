package br.com.navis.transportadora.externo.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TransportadoraRequest(
        @JsonProperty("razao_social") String razaoSocial,
        @JsonProperty("nome_fantasia") String nomeFantasia,
        @JsonProperty("num_cnpj") String numCnpj,
        @JsonProperty("created_at") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt
) {
}
