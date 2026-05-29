package com.example.zk;
import com.example.zk.service.*;

import com.example.zk.model.Department;
import com.example.zk.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repo;

    @InjectMocks
    private DepartmentService service;

    @Test
    void testFindAll() {

        Department d = new Department();
        d.setName("Finance");

        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(d));

        List<Department> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Finance", result.get(0).getName());
    }

    @Test
    void testSave() {

        Department d = new Department();
        d.setName("Finance");

        when(repo.save(d)).thenReturn(d);

        Department result = service.save(d);

        assertEquals("Finance", result.getName());
    }

    @Test
    void testDelete() {

        doNothing().when(repo).deleteById(1L);

        service.deleteById(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}