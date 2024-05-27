package ru.dlabs.sas.example.jsso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Контроллер управления WEB клиентом")
public class WebClientController {

    @GetMapping("/client/**")
    @Operation(description = "Контроллер получения WEB клиента (SPA приложение)")
    public String index() {
        return "index";
    }

}
