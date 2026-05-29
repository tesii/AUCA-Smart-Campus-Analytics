package com.example.zk.service;

import com.example.zk.model.Department;
import com.example.zk.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository repo;

    public List<Department> findAll() {
        return repo.findAllByOrderByNameAsc();
    }

    public Department save(Department d) {
        return repo.save(d);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
