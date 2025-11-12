package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.dto.Rfc1TransactionalResult;
import uniandes.edu.co.proyecto.service.RfcTransactionalService;

@RestController
@RequestMapping("/api/rfc1/transactional")
public class RfcTransactionalController {

    @Autowired
    private RfcTransactionalService rfcTransactionalService;

    @GetMapping("/read_committed")
    public ResponseEntity<Rfc1TransactionalResult> readCommitted(@RequestParam Long clienteId) {
        Rfc1TransactionalResult res = rfcTransactionalService.rfc1ReadCommitted(clienteId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/serializable")
    public ResponseEntity<Rfc1TransactionalResult> serializable(@RequestParam Long clienteId) {
        Rfc1TransactionalResult res = rfcTransactionalService.rfc1Serializable(clienteId);
        return ResponseEntity.ok(res);
    }
}
