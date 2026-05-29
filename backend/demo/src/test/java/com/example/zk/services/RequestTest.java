package com.example.zk;
import com.example.zk.service.*;
import com.example.zk.model.Request;
import com.example.zk.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepository repo;

    @InjectMocks
    private RequestService service;

    // ================= FIND ALL =================
    @Test
    void testFindAll_successStatus() {

        Request r1 = new Request();
        r1.setStatus("SUCCESS");

        when(repo.findAllByOrderByIdDesc())
                .thenReturn(List.of(r1));

        List<Request> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("SUCCESS", result.get(0).getStatus());
    }

    // ================= COUNT BY STATUS MAP =================
    @Test
void testCountByStatusMap_success() {

    Object[] row = new Object[]{"SUCCESS", 5L};

    List<Object[]> mockData = List.<Object[]>of(row);

    when(repo.countGroupedByStatus())
            .thenReturn(mockData);

    Map<String, Long> result = service.countByStatusMap();

    assertEquals(5L, result.get("SUCCESS"));
}

    // ================= COUNT BY TITLE =================
    @Test
void testCountByTitle() {

    Object[] row = new Object[]{"REQUEST_A", 3L};

    List<Object[]> mockData = List.<Object[]>of(row);

    when(repo.countGroupedByTitle())
            .thenReturn(mockData);

    List<Object[]> result = service.countByTitle();

    assertEquals("REQUEST_A", result.get(0)[0]);
    assertEquals(3L, result.get(0)[1]);
}
    // ================= SAVE =================
    @Test
    void testSave_success() {

        Request r = new Request();
        r.setStatus("SUCCESS");

        when(repo.save(r)).thenReturn(r);

        Request result = service.save(r);

        assertEquals("SUCCESS", result.getStatus());
    }

    // ================= DELETE =================
    @Test
    void testDelete() {

        doNothing().when(repo).deleteById(1L);

        service.deleteById(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}