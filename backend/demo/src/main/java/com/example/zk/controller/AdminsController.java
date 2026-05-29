package com.example.zk.controller;

import com.example.zk.model.Admin;
import com.example.zk.service.AdminService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

import java.util.List;

public class AdminsController extends SelectorComposer<Component> {

    @Wire("#adminsList")
    private Listbox adminsList;

    @Wire
    private Textbox usernameBox;

    @Wire
    private Textbox emailBox;

    @Wire
    private Textbox passwordBox;

    @Wire
    private Button createBtn;

    @WireVariable
    private AdminService adminService;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (adminService == null) {
            try {
                adminService = (AdminService) SpringUtil.getBean("adminService");
            } catch (Exception ignored) {
            }
        }
        refreshList();
    }

    private void refreshList() {
        List<Admin> allAdmins = adminService.findAll();
        ListModelList<Admin> model = new ListModelList<>(allAdmins);
        model.setMultiple(false);

        adminsList.setItemRenderer(new ListitemRenderer<Admin>() {
            @Override
            public void render(Listitem item, Admin admin, int index) throws Exception {
                item.setValue(admin);
                item.appendChild(new Listcell(String.valueOf(admin.getId())));
                item.appendChild(new Listcell(admin.getUsername()));
                item.appendChild(new Listcell(admin.getEmail()));
                Listcell actionsCell = new Listcell();
                Button deleteButton = new Button("Delete");
                deleteButton.setSclass("z-button");
                deleteButton.addEventListener("onClick", event -> {
                    adminService.deleteAdmin(admin.getId());
                    refreshList();
                });
                actionsCell.appendChild(deleteButton);
                item.appendChild(actionsCell);
            }
        });

        adminsList.setModel(model);
    }

    @Listen("onClick = #createBtn")
    public void create() {
        String username = usernameBox.getValue();
        String email = emailBox.getValue();
        String password = passwordBox.getValue();
        if (username == null || username.isBlank()) {
            return;
        }

        adminService.createAdmin(username.trim(), email == null ? "" : email.trim(), password == null ? "" : password);
        usernameBox.setValue("");
        emailBox.setValue("");
        passwordBox.setValue("");
        refreshList();
    }
}
