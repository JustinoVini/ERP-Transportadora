package br.com.navis.transportadora.externo.infra.service;

import br.com.navis.transportadora.config.exception.ValidationException;
import br.com.navis.transportadora.externo.domain.dto.TransportadoraRequest;
import br.com.navis.transportadora.externo.domain.dto.TransportadoraResponse;
import br.com.navis.transportadora.externo.domain.model.Transportadora;
import br.com.navis.transportadora.externo.domain.repository.TransportadoraRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class TransportadoraService {

    private final TransportadoraRepository transportadoraRepository;

    private final SchemaService schemaService;

    public TransportadoraService(TransportadoraRepository transportadoraRepository, SchemaService schemaService) {
        this.transportadoraRepository = transportadoraRepository;
        this.schemaService = schemaService;
    }

    public TransportadoraResponse create(TransportadoraRequest request) {
        validateShipperInformations(request);
        validateInformedTaxNumber(request);
        var transportadora = transportadoraRepository.save(Transportadora.of(request));
        schemaService.createSchema(transportadora.getId());
        return TransportadoraResponse.of(transportadora);
    }

    private void validateInformedTaxNumber(TransportadoraRequest request) {
        String numCnpj = request.numCnpj();

        Optional<Transportadora> existingTransportadora = Optional.ofNullable(transportadoraRepository.findByNumCnpj(numCnpj));

        if (existingTransportadora.isPresent()) {
            throw new ValidationException("Tax Number Already registered in the database");
        }
    }

    private void validateShipperInformations(TransportadoraRequest request) {

        if (isEmpty(request.razaoSocial())) {
            throw new ValidationException("Please inform the shipping name.");
        }

        if (isEmpty(request.nomeFantasia())) {
            throw new ValidationException("Please inform the fantasy name.");
        }

        if (isEmpty(request.numCnpj()) && request.numCnpj().length() < 14) {
            throw new ValidationException("Tax number format was not informed or malformed");
        }

    }

}
