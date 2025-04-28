package co.edu.uniandes.dse.asesorando.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes")
public class DefaultController {
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> welcome() {
        List<String> routes = new ArrayList<>();
        routes.add("GET /routes: Returns a list of all available routes.");
        return routes;
    }
}
