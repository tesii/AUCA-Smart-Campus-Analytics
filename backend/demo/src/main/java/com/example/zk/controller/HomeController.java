package com.example.zk.controller;

import com.example.zk.model.Admin;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class HomeController extends SelectorComposer<Component> {
@Wire
private Label welcomeLabel;

@Wire
private Label userLabel;
    @Wire
    private Div contentArea;

    private Admin admin;

  @Override
public void doAfterCompose(Component comp) throws Exception {
    super.doAfterCompose(comp);

    // =========================
    // GET LOGGED-IN ADMIN
    // =========================
    admin = (Admin) Executions.getCurrent()
            .getSession()
            .getAttribute("admin");

    if (admin == null) {
        Executions.sendRedirect("/zul/login.zul");
        return;
    }

    // =========================
    // SHOW LOGIN SUCCESS MESSAGE (ONLY ONCE)
    // =========================
    String loginSuccess = Executions.getCurrent().getParameter("loginSuccess");

    if ("true".equals(loginSuccess)) {
        Clients.showNotification(
                "Login successful!",
                "info",
                null,
                "middle_center",
                2000
        );
    }

    // =========================
    // UPDATE HEADER UI (WELCOME TEXT)
    // =========================
    if (welcomeLabel != null && userLabel != null) {
        String name = admin.getUsername(); // or getFullName()

        welcomeLabel.setValue("Welcome, " + name);
        userLabel.setValue("Logged in as: " + name);
    }

    // =========================
    // LOAD DEFAULT PAGE
    // =========================
    loadDashboard();
}
    // 🟢 DEFAULT DASHBOARD
    private void loadDashboard() {
        contentArea.getChildren().clear();
        contentArea.appendChild(new Label("Welcome " + admin.getUsername()));
    }

    // 🟢 LOAD ADMINS PAGE
    private void loadAdmins() {
        contentArea.getChildren().clear();
        org.zkoss.zk.ui.Component comp = org.zkoss.zk.ui.Executions.createComponents("/zul/admins.zul", null, null);
        contentArea.appendChild(comp);
    }

    // 🟢 LOAD REPORTS PAGE
    private void loadReports() {
        contentArea.getChildren().clear();
        org.zkoss.zk.ui.Component comp = org.zkoss.zk.ui.Executions.createComponents("/zul/reports.zul", null, null);
        contentArea.appendChild(comp);
    }

    // 🟢 LOAD DEPARTMENTS
    private void loadDepartments() {
        contentArea.getChildren().clear();
        org.zkoss.zk.ui.Component comp = org.zkoss.zk.ui.Executions.createComponents("/zul/departments.zul", null, null);
        contentArea.appendChild(comp);
    }

    // 🟢 LOAD ROLES
    private void loadRoles() {
        contentArea.getChildren().clear();
        org.zkoss.zk.ui.Component comp = org.zkoss.zk.ui.Executions.createComponents("/zul/roles.zul", null, null);
        contentArea.appendChild(comp);
    }

    // 🟢 LOAD STAFF
    private void loadStaff() {
        contentArea.getChildren().clear();
        org.zkoss.zk.ui.Component comp = org.zkoss.zk.ui.Executions.createComponents("/zul/staff.zul", null, null);
        contentArea.appendChild(comp);
    }

    // 🔥 BUTTON EVENTS

    @Listen("onClick = #btnDashboard")
    public void dashboard() {
        loadDashboard();
    }

    @Listen("onClick = #btnAdmins")
    public void admins() {
        loadAdmins();
    }

    @Listen("onClick = #btnDepartments")
    public void departments() {
        loadDepartments();
    }

    @Listen("onClick = #btnRoles")
    public void roles() {
        loadRoles();
    }

    @Listen("onClick = #btnStaff")
    public void staff() {
        loadStaff();
    }

    @Listen("onClick = #btnReports")
    public void reports() {
        loadReports();
    }

    @Listen("onClick = #logoutBtn")
    public void logout() {
        Executions.getCurrent().getSession().removeAttribute("admin");
        Executions.sendRedirect("/zul/login.zul");
    }
}