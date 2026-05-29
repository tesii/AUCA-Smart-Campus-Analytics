package com.example.zk;
import com.example.zk.service.*;
import com.example.zk.model.Staff;
import com.example.zk.repository.StaffRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    private StaffRepository repo;

    @InjectMocks
    private StaffService staffService;

    // ================= FIND ALL =================
    @Test
    void testFindAll() {

        Staff alice = new Staff();
        alice.setId(1L);
        alice.setFullName("Alice");

        when(repo.findAllByOrderByFullNameAsc())
                .thenReturn(List.of(alice));

        List<Staff> result = staffService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getFullName());

        verify(repo, times(1)).findAllByOrderByFullNameAsc();
    }

    // ================= SAVE =================
    @Test
    void testSave() {

        Staff alice = new Staff();
        alice.setFullName("Alice");

        when(repo.save(alice)).thenReturn(alice);

        Staff result = staffService.save(alice);

        assertNotNull(result);
        assertEquals("Alice", result.getFullName());

        verify(repo, times(1)).save(alice);
    }

    // ================= DELETE =================
    @Test
    void testDeleteById() {

        doNothing().when(repo).deleteById(1L);

        staffService.deleteById(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}