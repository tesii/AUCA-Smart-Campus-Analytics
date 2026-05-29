package com.example.zk;

import com.example.zk.service.*;

import com.example.zk.model.Admin;
import com.example.zk.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository repo;

    @InjectMocks
    private AdminService service;

    // ================= CREATE ADMIN =================
    @Test
    void testCreateAdmin() {

        Admin admin = new Admin();
        admin.setUsername("alice");
        admin.setEmail("alice@gmail.com");
        admin.setPassword("1234");

        when(repo.save(any(Admin.class))).thenReturn(admin);

        Admin result = service.createAdmin("alice", "alice@gmail.com", "1234");

        assertEquals("alice", result.getUsername());
        assertEquals("alice@gmail.com", result.getEmail());
        assertEquals("1234", result.getPassword());
    }

    // ================= FIND ALL =================
    @Test
    void testFindAll() {

        Admin admin = new Admin();
        admin.setUsername("alice");

        when(repo.findAllByOrderByUsernameAsc())
                .thenReturn(List.of(admin));

        List<Admin> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("alice", result.get(0).getUsername());
    }

    // ================= LOGIN SUCCESS (plain password) =================
    @Test
    void testLogin_success_plainPassword() {

        Admin admin = new Admin();
        admin.setUsername("alice");
        admin.setEmail("alice@gmail.com");
        admin.setPassword("1234"); // plain password case

        when(repo.findByUsername("alice"))
                .thenReturn(Optional.of(admin));

        Admin result = service.login("alice", "1234");

        assertNotNull(result);
        assertEquals("alice", result.getUsername());
    }

    // ================= LOGIN FAILURE =================
    @Test
    void testLogin_failure_wrongPassword() {

        Admin admin = new Admin();
        admin.setUsername("alice");
        admin.setPassword("1234");

        when(repo.findByUsername("alice"))
                .thenReturn(Optional.of(admin));

        Admin result = service.login("alice", "wrong");

        assertNull(result);
    }

    // ================= DELETE ADMIN =================
    @Test
    void testDeleteAdmin() {

        doNothing().when(repo).deleteById(1L);

        service.deleteAdmin(1L);

        verify(repo, times(1)).deleteById(1L);
    }
}