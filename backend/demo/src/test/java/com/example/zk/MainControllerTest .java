package com.example.zk;

import com.example.zk.controller.*;
import com.example.zk.repository.*;
import com.example.zk.model.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ================= MOCK REPOSITORIES =================

    @MockBean
    private StaffRepository staffRepo;

    @MockBean
    private DepartmentRepository departmentRepo;

    @MockBean
    private RoleRepository roleRepo;

    @MockBean
    private RequestRepository requestRepo;

    // ================= STAFF =================

    @Test
    void testGetStaff() throws Exception {
        Staff s = new Staff();
        s.setFullName("Alice");

        when(staffRepo.findAll()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/dashboard/staff"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Alice"));
    }

    // ================= DEPARTMENTS =================

    @Test
    void testGetDepartments() throws Exception {
        Department d = new Department();
        d.setName("Finance");

        when(departmentRepo.findAll()).thenReturn(List.of(d));

        mockMvc.perform(get("/api/dashboard/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Finance"));
    }

    // ================= ROLES =================

    @Test
    void testGetRoles() throws Exception {
        Role r = new Role();
        r.setName("ADMIN");

        when(roleRepo.findAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/dashboard/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ADMIN"));
    }

    // ================= REQUESTS =================

    @Test
    void testGetRequests() throws Exception {
        Request req = new Request();
        req.setTitle("Login issue");

        when(requestRepo.findAll()).thenReturn(List.of(req));

        mockMvc.perform(get("/api/dashboard/requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Login issue"));
    }

    // ================= STATS =================

    @Test
    void testStaffCount() throws Exception {
        when(staffRepo.count()).thenReturn(10L);

        mockMvc.perform(get("/api/dashboard/stats/staff-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void testActiveStaff() throws Exception {
        when(staffRepo.countByStatus("ACTIVE")).thenReturn(5L);

        mockMvc.perform(get("/api/dashboard/stats/active-staff"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testDepartmentsCount() throws Exception {
        when(departmentRepo.count()).thenReturn(3L);

        mockMvc.perform(get("/api/dashboard/stats/departments-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void testRolesCount() throws Exception {
        when(roleRepo.count()).thenReturn(2L);

        mockMvc.perform(get("/api/dashboard/stats/roles-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void testRequestsCount() throws Exception {
        when(requestRepo.count()).thenReturn(7L);

        mockMvc.perform(get("/api/dashboard/stats/requests-count"))
                .andExpect(status().isOk())
                .andExpect(content().string("7"));
    }

    @Test
    void testPendingRequests() throws Exception {
        when(requestRepo.countByStatus("PENDING")).thenReturn(4L);

        mockMvc.perform(get("/api/dashboard/stats/pending-requests"))
                .andExpect(status().isOk())
                .andExpect(content().string("4"));
    }
}