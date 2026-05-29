package com.example.zk.controller;

import com.example.zk.model.Role;
import com.example.zk.service.RoleService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.*;

import java.util.List;

public class RoleController extends SelectorComposer<Component> {

    @Wire
    private Listbox roleList;

    @Wire
    private Textbox nameBox;

    @WireVariable
    private RoleService roleService;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        if (roleService == null) {
            try {
                roleService = (RoleService) SpringUtil.getBean("roleService");
            } catch (Exception ignored) {
            }
        }

        refresh();
    }

    // =========================
    // REFRESH TABLE
    // =========================
    private void refresh() {

        roleList.getItems().clear();

        List<Role> all = roleService.findAll();

        for (Role r : all) {

            Listitem li = new Listitem();
            li.setValue(r);

            // ID
            li.appendChild(new Listcell(String.valueOf(r.getId())));

            // NAME
            li.appendChild(new Listcell(r.getName()));

            // ===== ACTION CELL =====
            Listcell actionCell = new Listcell();

            // EDIT BUTTON
            Button editBtn = new Button("Edit");
            editBtn.setSclass("btn-edit");

            editBtn.addEventListener("onClick", e -> {

                String newName = Messagebox.show(
                        "Edit functionality coming next",
                        "Edit Role",
                        Messagebox.OK,
                        Messagebox.INFORMATION
                ) + "";

                nameBox.setValue(r.getName());
            });

            // DELETE BUTTON
            Button deleteBtn = new Button("Delete");
            deleteBtn.setSclass("btn-delete");

            deleteBtn.addEventListener("onClick", e -> {

                Messagebox.show(
                        "Delete this role?",
                        "Confirm Delete",
                        Messagebox.YES | Messagebox.NO,
                        Messagebox.QUESTION,
                        event -> {

                            if (Messagebox.ON_YES.equals(event.getName())) {

                                roleService.deleteById(r.getId());

                                refresh();

                                Clients.showNotification(
                                        "Role deleted successfully",
                                        "info",
                                        null,
                                        "top_center",
                                        2000
                                );
                            }
                        }
                );
            });

            actionCell.appendChild(editBtn);
            actionCell.appendChild(new Space());
            actionCell.appendChild(deleteBtn);

            li.appendChild(actionCell);

            roleList.appendChild(li);
        }
    }

    // =========================
    // CREATE ROLE
    // =========================
    @Listen("onClick = #createRoleBtn")
    public void create() {

        String name = nameBox.getValue();

        if (name == null || name.isBlank()) {
            return;
        }

        Role r = new Role();
        r.setName(name.trim());

        roleService.save(r);

        nameBox.setValue("");

        refresh();

        Clients.showNotification(
                "Role created successfully",
                "info",
                null,
                "top_center",
                2000
        );
    }
}