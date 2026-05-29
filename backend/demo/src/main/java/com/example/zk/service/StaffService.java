package com.example.zk.service;
import com.example.zk.model.Staff;
import com.example.zk.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository repo;

    public List<Staff> findAll() {
        return repo.findAllByOrderByFullNameAsc();
    }

    public Staff save(Staff s) {
        return repo.save(s);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}