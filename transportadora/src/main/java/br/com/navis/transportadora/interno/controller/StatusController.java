package br.com.navis.transportadora.interno.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping("/")
    public String getText() {
        return "API de Transportadora Rodando";
    }
}
