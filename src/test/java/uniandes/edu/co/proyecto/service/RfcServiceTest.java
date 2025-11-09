package uniandes.edu.co.proyecto.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import uniandes.edu.co.proyecto.dto.Rfc1Dto;
import uniandes.edu.co.proyecto.dto.Rfc2Dto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RfcServiceTest {

    @Mock
    NamedParameterJdbcTemplate jdbc;

    @InjectMocks
    RfcService service;

    @Test
    void rfc1_shouldReturnMappedList() {
        Rfc1Dto dto = new Rfc1Dto();
        dto.idServicio = 42L;
        when(jdbc.query(anyString(), any(MapSqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(List.of(dto));

        List<Rfc1Dto> result = service.rfc1(123L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(42L, result.get(0).idServicio.longValue());
    }

    @Test
    void rfc2_shouldReturnMappedList() {
        Rfc2Dto d = new Rfc2Dto();
        d.conductorId = 7L;
        d.nombreConductor = "Test Driver";
        when(jdbc.query(anyString(), any(RowMapper.class))).thenReturn(List.of(d));

        List<Rfc2Dto> res = service.rfc2();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(7L, res.get(0).conductorId.longValue());
        assertEquals("Test Driver", res.get(0).nombreConductor);
    }
}
