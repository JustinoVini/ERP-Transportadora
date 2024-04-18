package br.com.navis.transportadora.externo.domain.dto;

import br.com.navis.transportadora.externo.domain.model.Transportadora;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransportadoraResponse(
        Long id,
        String razaoSocial,
        String nomeFantasia,
        String numCnpj,
        @JsonProperty("created_at") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
        @JsonProperty("updated_at") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updatedAt
) {

    public static TransportadoraResponse of(Transportadora transportadora) {
        return TransportadoraResponse.builder()
                .id(transportadora.getId())
                .razaoSocial(transportadora.getRazaoSocial())
                .nomeFantasia(transportadora.getNomeFantasia())
                .numCnpj(transportadora.getNumCnpj())
                .createdAt(transportadora.getCreatedAt())
                .updatedAt(transportadora.getUpdatedAt())
                .build();
    }

}
