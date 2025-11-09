package uniandes.edu.co.proyecto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uniandes.edu.co.proyecto.dto.Rfc2Dto;
import uniandes.edu.co.proyecto.service.RfcService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RfcControllerTest {

    @Mock
    RfcService service;

    @InjectMocks
    RfcController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void rfc2_endpointReturnsJson() throws Exception {
        Rfc2Dto d = new Rfc2Dto();
        d.conductorId = 5L;
        d.nombreConductor = "Driver 5";
        when(service.rfc2()).thenReturn(List.of(d));

        mockMvc.perform(get("/api/rfcs/rfc2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].conductorId").value(5))
                .andExpect(jsonPath("$[0].nombreConductor").value("Driver 5"));
    }
}
