package com.example.zk.repository;

import com.example.zk.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    long countByStatus(String status);

    List<Staff> findAllByOrderByFullNameAsc();
}