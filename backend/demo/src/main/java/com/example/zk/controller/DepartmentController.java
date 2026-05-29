package com.example.zk.controller;

import com.example.zk.model.Department;
import com.example.zk.service.DepartmentService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.*;

import java.util.List;

public class DepartmentController extends SelectorComposer<Component> {

    @Wire
    private Listbox deptList;

    @Wire
    private Textbox nameBox, codeBox, descBox;

    @Wire
    private Button createDeptBtn;

    @WireVariable
    private DepartmentService departmentService;

    private Department selectedDept; // 👈 for edit mode

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        if (departmentService == null) {
            try {
                departmentService = (DepartmentService) SpringUtil.getBean("departmentService");
            } catch (Exception ignored) {}
        }

        refresh();
    }

    // =========================
    // REFRESH TABLE
    // =========================
    private void refresh() {

        deptList.getItems().clear();

        List<Department> all = departmentService.findAll();

        for (Department d : all) {

            Listitem li = new Listitem();
            li.setValue(d);

            li.appendChild(new Listcell(String.valueOf(d.getId())));
            li.appendChild(new Listcell(d.getName()));
            li.appendChild(new Listcell(d.getCode()));
            li.appendChild(new Listcell(d.getDescription()));

            // ================= ACTIONS =================
            Listcell actionCell = new Listcell();

            // EDIT BUTTON
            Button editBtn = new Button("Edit");
            editBtn.setSclass("btn-edit");

            editBtn.addEventListener("onClick", e -> {
                selectedDept = d;

                nameBox.setValue(d.getName());
                codeBox.setValue(d.getCode());
                descBox.setValue(d.getDescription());

                createDeptBtn.setLabel("Update Department");
            });

            // DELETE BUTTON
            Button deleteBtn = new Button("Delete");
            deleteBtn.setSclass("btn-delete");

            deleteBtn.addEventListener("onClick", e -> {

                Messagebox.show(
                        "Are you sure you want to delete this department?",
                        "Confirm Delete",
                        Messagebox.YES | Messagebox.NO,
                        Messagebox.QUESTION,
                        event -> {

                            if (Messagebox.ON_YES.equals(event.getName())) {

                                departmentService.deleteById(d.getId());
                                refresh();

                                Clients.showNotification(
                                        "Department deleted successfully",
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

            deptList.appendChild(li);
        }
    }

    // =========================
    // CREATE / UPDATE
    // =========================
    @Listen("onClick = #createDeptBtn")
    public void createOrUpdate() {

        if (nameBox.getValue() == null || nameBox.getValue().isBlank()) return;

        if (selectedDept != null) {
            // UPDATE
            selectedDept.setName(nameBox.getValue());
            selectedDept.setCode(codeBox.getValue());
            selectedDept.setDescription(descBox.getValue());

            departmentService.save(selectedDept);

            selectedDept = null;
            createDeptBtn.setLabel("Create Department");

        } else {
            // CREATE
            Department d = new Department();
            d.setName(nameBox.getValue());
            d.setCode(codeBox.getValue());
            d.setDescription(descBox.getValue());

            departmentService.save(d);
        }

        nameBox.setValue("");
        codeBox.setValue("");
        descBox.setValue("");

        refresh();

        Clients.showNotification(
                "Saved successfully",
                "info",
                null,
                "top_center",
                2000
        );
    }
}