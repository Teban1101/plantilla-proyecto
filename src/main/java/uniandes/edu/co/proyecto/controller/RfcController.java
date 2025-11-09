package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.dto.Rfc1Dto;
import uniandes.edu.co.proyecto.dto.Rfc2Dto;
import uniandes.edu.co.proyecto.dto.Rfc3Dto;
import uniandes.edu.co.proyecto.dto.Rfc4Dto;
import uniandes.edu.co.proyecto.service.RfcService;

import java.util.List;

@RestController
@RequestMapping("/api/rfcs")
public class RfcController {

    private final RfcService service;

    public RfcController(RfcService service) {
        this.service = service;
    }

    @GetMapping("/rfc1")
    public List<Rfc1Dto> rfc1(@RequestParam(name = "clienteId") Long clienteId) {
        return service.rfc1(clienteId);
    }

    @GetMapping("/rfc2")
    public List<Rfc2Dto> rfc2() {
        return service.rfc2();
    }

    @GetMapping("/rfc3")
    public List<Rfc3Dto> rfc3() {
        return service.rfc3();
    }

    @GetMapping("/rfc4")
    public List<Rfc4Dto> rfc4(@RequestParam String ciudad,
                              @RequestParam(name = "fechaInicio") String fechaInicio,
                              @RequestParam(name = "fechaFin") String fechaFin) {
        return service.rfc4(ciudad, fechaInicio, fechaFin);
    }
}
