package com.example.zk;
import com.example.zk.service.*;
import com.example.zk.model.Role;
import com.example.zk.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repo;

    @InjectMocks
    private RoleService service;

    @Test
    void testFindAll() {

        Role role = new Role();
        role.setName("MANAGER");

        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(role));

        List<Role> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("MANAGER", result.get(0).getName());
    }

    @Test
    void testSave() {

        Role role = new Role();
        role.setName("MANAGER");

        when(repo.save(role)).thenReturn(role);

        Role result = service.save(role);

        assertEquals("MANAGER", result.getName());
    }

    @Test
    void testDelete() {

        doNothing().when(repo).deleteById(1L);

        service.deleteById(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}