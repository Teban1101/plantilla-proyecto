package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.dto.Rfc1Dto;
import uniandes.edu.co.proyecto.dto.Rfc1TransactionalResult;

import java.util.List;

@Service
public class RfcTransactionalService {

    private final RfcService rfcService;

    public RfcTransactionalService(RfcService rfcService) {
        this.rfcService = rfcService;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Rfc1TransactionalResult rfc1ReadCommitted(Long clienteId) {
        List<Rfc1Dto> first = rfcService.rfc1(clienteId);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Rfc1Dto> second = rfcService.rfc1(clienteId);
        return new Rfc1TransactionalResult(first, second);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Rfc1TransactionalResult rfc1Serializable(Long clienteId) {
        List<Rfc1Dto> first = rfcService.rfc1(clienteId);

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Rfc1Dto> second = rfcService.rfc1(clienteId);
        return new Rfc1TransactionalResult(first, second);
    }
}
