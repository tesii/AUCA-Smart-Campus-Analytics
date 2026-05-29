package com.example.zk.service;

import com.example.zk.model.Role;
import com.example.zk.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repo;

    public List<Role> findAll() {
        return repo.findAllByOrderByNameAsc();
    }

    public Role save(Role r) {
        return repo.save(r);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
