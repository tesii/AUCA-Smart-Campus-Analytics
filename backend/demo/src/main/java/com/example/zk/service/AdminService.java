package com.example.zk.service;

import com.example.zk.model.Admin;
import com.example.zk.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin login(String identifier, String password) {
        if (identifier == null || identifier.trim().isEmpty()) {
            log.debug("login: empty identifier");
            return null;
        }

        identifier = identifier.trim();
        Optional<Admin> adminOpt = adminRepository.findByUsername(identifier);
        if (adminOpt.isEmpty()) {
            adminOpt = adminRepository.findByEmail(identifier);
        }

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            String stored = admin.getPassword();
            if (stored == null) {
                log.debug("login: user {} has no password set", identifier);
                return null;
            }

            try {
                if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
                    BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
                    if (enc.matches(password, stored)) {
                        return admin;
                    }
                }
            } catch (NoClassDefFoundError ex) {
                log.debug("BCrypt encoder not available on classpath");
            } catch (Exception e) {
                log.debug("BCrypt check failed: {}", e.getMessage());
            }

            if (stored.equals(password)) {
                return admin;
            }

            log.debug("login: password mismatch for user {}", identifier);
        } else {
            log.debug("login: user not found for identifier {}", identifier);
        }

        return null;
    }

    public List<Admin> findAll() {
        return adminRepository.findAllByOrderByUsernameAsc();
    }

    public Admin createAdmin(String username, String email, String password) {
        Admin a = new Admin();
        a.setUsername(username);
        a.setEmail(email);
        a.setPassword(password);
        return adminRepository.save(a);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }
}