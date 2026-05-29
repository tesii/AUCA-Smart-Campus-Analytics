package com.example.zk.controller;

import com.example.zk.model.Admin;
import com.example.zk.service.AdminService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zkplus.spring.SpringUtil;

public class LoginController extends SelectorComposer<Component> {

    @Wire
    private Textbox username;

    @Wire
    private Textbox password;

    @Wire
    private Label message;

    @WireVariable
    private AdminService adminService;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        // Fallback: if ZK failed to inject via @WireVariable, get bean from Spring context
        if (this.adminService == null) {
            try {
                this.adminService = (AdminService) SpringUtil.getBean("adminService");
            } catch (Exception e) {
                // leave null; processLogin will handle service absence
            }
        }
    }

    @Listen("onClick = #loginBtn")
    public void login() {
        processLogin();
    }

    @Listen("onOK = #username")
    public void loginOnEnterUsername() {
        processLogin();
    }

    @Listen("onOK = #password")
    public void loginOnEnterPassword() {
        processLogin();
    }

    private void processLogin() {
        if (this.adminService == null) {
            message.setStyle("color:red;");
            message.setValue("Server error: authentication service unavailable");
            return;
        }
        Admin admin = adminService.login(
                username.getValue(),
                password.getValue()
        );

        if (admin != null) {
            Executions.getCurrent().getSession().setAttribute("admin", admin);
            message.setStyle("color:green;");
            message.setValue("Login successful. Redirecting to home...");
            Clients.showNotification("Login successful", "info", null, "middle_center", 1500);
            String target = "/zul/home.zul?loginSuccess=true";
            Clients.evalJavaScript("setTimeout(function(){window.location='" + target + "';}, 1500);");
        } else {
            message.setStyle("color:red;");
            message.setValue("Invalid username or password");
        }
    }
}