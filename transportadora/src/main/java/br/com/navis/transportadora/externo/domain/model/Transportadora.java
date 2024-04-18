package br.com.navis.transportadora.externo.domain.model;

import br.com.navis.transportadora.externo.domain.dto.TransportadoraRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transportadora", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(name = "num_cnpj", length = 14, nullable = false)
    private String numCnpj;

    /**
     * @see Colunas que não são alteraveis
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * @see Esses métodos fazem com que o JPA entenda que esses dados só serão
     *      alterados quando criar ou atualizar
     */
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Transportadora of(TransportadoraRequest request) {
        return Transportadora.builder()
                .razaoSocial(request.razaoSocial())
                .nomeFantasia(request.nomeFantasia())
                .numCnpj(request.numCnpj())
                .build();
    }

}
