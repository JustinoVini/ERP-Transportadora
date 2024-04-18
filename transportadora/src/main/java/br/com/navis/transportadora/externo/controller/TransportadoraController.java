package br.com.navis.transportadora.externo.controller;

import br.com.navis.transportadora.externo.domain.dto.TransportadoraRequest;
import br.com.navis.transportadora.externo.domain.dto.TransportadoraResponse;
import br.com.navis.transportadora.externo.infra.service.TransportadoraService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transportadora")
public class TransportadoraController {

    private final TransportadoraService transportadoraService;

    public TransportadoraController(TransportadoraService transportadoraService) {
        this.transportadoraService = transportadoraService;
    }

    @PostMapping("create")
    public TransportadoraResponse save(@RequestBody TransportadoraRequest request) {
        return transportadoraService.create(request);
    }

}
