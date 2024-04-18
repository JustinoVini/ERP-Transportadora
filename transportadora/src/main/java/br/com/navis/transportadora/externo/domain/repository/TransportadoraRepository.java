package br.com.navis.transportadora.externo.domain.repository;

import br.com.navis.transportadora.externo.domain.model.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {

    Transportadora findByNumCnpj(String numCnpj);

}
