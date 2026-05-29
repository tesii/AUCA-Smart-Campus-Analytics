package com.example.zk.zk;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zkoss.bind.annotation.Init;

@Component
public class DashboardViewModel {

    private final RestTemplate restTemplate = new RestTemplate();

    private long staffCount;
    private long activeStaff;
    private long departmentCount;
    private long roleCount;
    private long requestCount;
    private long pendingRequests;

    private List staffList;

    @Init
    public void init() {

        String baseUrl = "http://localhost:8080/api/dashboard";

        try {
            staffCount = restTemplate.getForObject(baseUrl + "/stats/staff-count", Long.class);
            activeStaff = restTemplate.getForObject(baseUrl + "/stats/active-staff", Long.class);
            departmentCount = restTemplate.getForObject(baseUrl + "/stats/departments-count", Long.class);
            roleCount = restTemplate.getForObject(baseUrl + "/stats/roles-count", Long.class);
            requestCount = restTemplate.getForObject(baseUrl + "/stats/requests-count", Long.class);
            pendingRequests = restTemplate.getForObject(baseUrl + "/stats/pending-requests", Long.class);

            staffList = restTemplate.getForObject(baseUrl + "/staff", List.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GETTERS (required for ZK binding)

    public long getStaffCount() {
        return staffCount;
    }

    public long getActiveStaff() {
        return activeStaff;
    }

    public long getDepartmentCount() {
        return departmentCount;
    }

    public long getRoleCount() {
        return roleCount;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public long getPendingRequests() {
        return pendingRequests;
    }

    public List getStaffList() {
        return staffList;
    }
}