package com.example.zk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.zk.model.Staff;
import com.example.zk.model.Department;
import com.example.zk.model.Role;
import com.example.zk.model.Request;

import com.example.zk.repository.StaffRepository;
import com.example.zk.repository.DepartmentRepository;
import com.example.zk.repository.RoleRepository;
import com.example.zk.repository.RequestRepository;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class MainController {

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private RequestRepository requestRepo;

    // =========================
    // LIST DATA (FOR ZK TABLES)
    // =========================

    @GetMapping("/staff")
    public List<Staff> getStaff() {
        return staffRepo.findAll();
    }

    @GetMapping("/departments")
    public List<Department> getDepartments() {
        return departmentRepo.findAll();
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @GetMapping("/requests")
    public List<Request> getRequests() {
        return requestRepo.findAll();
    }

    // =========================
    // ANALYTICS (FOR DASHBOARD CARDS)
    // =========================

    @GetMapping("/stats/staff-count")
    public long staffCount() {
        return staffRepo.count();
    }

    @GetMapping("/stats/active-staff")
    public long activeStaff() {
        return staffRepo.countByStatus("ACTIVE");
    }

    @GetMapping("/stats/departments-count")
    public long departmentCount() {
        return departmentRepo.count();
    }

    @GetMapping("/stats/roles-count")
    public long roleCount() {
        return roleRepo.count();
    }

    @GetMapping("/stats/requests-count")
    public long requestCount() {
        return requestRepo.count();
    }

    @GetMapping("/stats/pending-requests")
    public long pendingRequests() {
        return requestRepo.countByStatus("PENDING");
    }
}