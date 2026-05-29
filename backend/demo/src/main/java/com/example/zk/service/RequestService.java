package com.example.zk.service;

import com.example.zk.model.Request;
import com.example.zk.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository repo;

    public List<Request> findAll() {
        return repo.findAllByOrderByIdDesc();
    }

    public Map<String, Long> countByStatusMap() {
        return repo.countGroupedByStatus().stream()
                .collect(Collectors.toMap(
                        row -> row[0] == null ? "UNKNOWN" : (String) row[0],
                        row -> (Long) row[1]
                ));
    }

    // returns all titles ordered by count DESC — first is most, last is least
    public List<Object[]> countByTitle() {
        return repo.countGroupedByTitle();
    }

    public Request save(Request r) {
        return repo.save(r);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}