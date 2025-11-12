package uniandes.edu.co.proyecto.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.dto.Rfc1TransactionalResult;
import uniandes.edu.co.proyecto.modelo.Servicio;
import uniandes.edu.co.proyecto.repositorio.ServicioRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RfcTransactionalIntegrationTest {

    @Autowired
    private RfcTransactionalService rfcTransactionalService;

    @Autowired
    private ServicioRepository servicioRepository;

    @Test
    public void testSerializableSeesSameDataAcrossDelay() throws Exception {
        // Prepare: ensure there is no servicio for cliente 5555
        Long clienteId = 5555L;
        servicioRepository.deleteAll();

        // Start serializable transaction in a separate thread
        Thread t = new Thread(() -> {
            Rfc1TransactionalResult res = rfcTransactionalService.rfc1Serializable(clienteId);
            // both lists should be equal (no concurrent insert observed)
            assertEquals(res.before.size(), res.after.size());
        });
        t.start();

        // Wait a bit to ensure the transaction started and executed first query
        Thread.sleep(5000);

        // Insert a servicio concurrently (this would be the RF8 operation)
        Servicio s = new Servicio();
        s.setIdServicio(99999L);
        s.setTipoServicio("Estandar");
        s.setClienteId(clienteId);
        s.setDistanciaKm(1.0);
        s.setHoraInicio(java.time.LocalDateTime.now());
        servicioRepository.save(s);

        // Wait for thread to finish
        t.join(40000);
    }

    @Test
    public void testReadCommittedMaySeeNewData() throws Exception {
        Long clienteId = 6666L;
        servicioRepository.deleteAll();

        Thread t = new Thread(() -> {
            Rfc1TransactionalResult res = rfcTransactionalService.rfc1ReadCommitted(clienteId);
            // for read committed, after the delay the second query may see the new row -> sizes can differ
            assertNotNull(res.before);
            assertNotNull(res.after);
        });
        t.start();

        Thread.sleep(5000);

        Servicio s = new Servicio();
        s.setIdServicio(99998L);
        s.setTipoServicio("Estandar");
        s.setClienteId(clienteId);
        s.setDistanciaKm(1.0);
        s.setHoraInicio(java.time.LocalDateTime.now());
        servicioRepository.save(s);

        t.join(40000);
    }
}
