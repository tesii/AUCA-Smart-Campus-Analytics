package com.example.zk.controller;

import com.example.zk.model.Department;
import com.example.zk.model.Role;
import com.example.zk.model.Staff;
import com.example.zk.service.DepartmentService;
import com.example.zk.service.RoleService;
import com.example.zk.service.StaffService;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import java.util.List;

public class StaffController extends SelectorComposer<Component> {

    @Wire
    private Listbox staffList;

    @Wire
    private Textbox codeBox, nameBox, emailBox, phoneBox;

    @Wire
    private Combobox deptCombo, roleCombo;

    @WireVariable
    private StaffService staffService;

    @WireVariable
    private DepartmentService departmentService;

    @WireVariable
    private RoleService roleService;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (staffService == null) {
            try {
                staffService = (StaffService) SpringUtil.getBean("staffService");
            } catch (Exception ignored) {
            }
        }
        if (departmentService == null) {
            try {
                departmentService = (DepartmentService) SpringUtil.getBean("departmentService");
            } catch (Exception ignored) {
            }
        }
        if (roleService == null) {
            try {
                roleService = (RoleService) SpringUtil.getBean("roleService");
            } catch (Exception ignored) {
            }
        }
        refresh();
        loadChoices();
    }

  private void refresh() {

    List<Staff> all = staffService.findAll();

    ListModelList<Staff> model = new ListModelList<>(all);
    model.setMultiple(false);

    staffList.setModel(model);

    staffList.setItemRenderer(new ListitemRenderer<Staff>() {

        @Override
        public void render(Listitem item, Staff staff, int index) {

            item.setValue(staff);

            item.appendChild(new Listcell(String.valueOf(staff.getId())));
            item.appendChild(new Listcell(staff.getStaffCode()));
            item.appendChild(new Listcell(staff.getFullName()));
            item.appendChild(new Listcell(staff.getEmail()));

            item.appendChild(new Listcell(
                    staff.getPhone() == null ? "" : staff.getPhone()
            ));

            item.appendChild(new Listcell(
                    staff.getDepartment() == null
                            ? ""
                            : staff.getDepartment().getName()
            ));

            item.appendChild(new Listcell(
                    staff.getRole() == null
                            ? ""
                            : staff.getRole().getName()
            ));

            // =========================
            // DELETE BUTTON
            // =========================

            Button deleteBtn = new Button("Delete");
            deleteBtn.setSclass("btn-delete");

            deleteBtn.addEventListener("onClick", event -> {

                Messagebox.show(
                        "Delete \"" + staff.getFullName() + "\" ?",
                        "Confirm Delete",
                        Messagebox.OK | Messagebox.CANCEL,
                        Messagebox.QUESTION,

                        click -> {
                            if (click.getName().equals(Messagebox.ON_OK)) {

                                staffService.deleteById(staff.getId());

                                refresh();
                            }
                        }
                );
            });

            Listcell actionCell = new Listcell();
            actionCell.appendChild(deleteBtn);

            item.appendChild(actionCell);
        }
    });
}
    private void loadChoices() {
        deptCombo.getItems().clear();
        roleCombo.getItems().clear();

        if (departmentService != null) {
            for (Department d : departmentService.findAll()) {
                Comboitem item = new Comboitem(d.getName());
                item.setValue(d);
                deptCombo.appendChild(item);
            }
        }
        if (roleService != null) {
            for (Role r : roleService.findAll()) {
                Comboitem item = new Comboitem(r.getName());
                item.setValue(r);
                roleCombo.appendChild(item);
            }
        }
    }

    @Listen("onClick = #createStaffBtn")
    public void create() {
        Staff s = new Staff();
        s.setStaffCode(codeBox.getValue());
        s.setFullName(nameBox.getValue());
        s.setEmail(emailBox.getValue());
        s.setPhone(phoneBox.getValue());
        s.setDepartment(deptCombo.getSelectedItem() == null ? null : (Department) deptCombo.getSelectedItem().getValue());
        s.setRole(roleCombo.getSelectedItem() == null ? null : (Role) roleCombo.getSelectedItem().getValue());
        staffService.save(s);
        codeBox.setValue("");
        nameBox.setValue("");
        emailBox.setValue("");
        phoneBox.setValue("");
        deptCombo.setValue("");
        roleCombo.setValue("");
        refresh();
    }
}
