package com.example.zk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.zk.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    long countByStatus(String status);

    List<Request> findAllByOrderByIdDesc();
     @Query("SELECT r.status, COUNT(r) FROM Request r GROUP BY r.status")
    List<Object[]> countGroupedByStatus();
    @Query("SELECT r.title, COUNT(r) FROM Request r GROUP BY r.title ORDER BY COUNT(r) DESC")
List<Object[]> countGroupedByTitle();
}