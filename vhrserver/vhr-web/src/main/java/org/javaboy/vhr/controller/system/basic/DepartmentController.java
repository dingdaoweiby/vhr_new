package org.javaboy.vhr.controller.system.basic;

import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PostMapping("/")
    public RespBean addDep(@RequestBody Department dep) {
        departmentService.addDep(dep);
        if (dep.getResult() == 1) {
            return RespBean.ok("Successfully Added!!", dep);
        }
        return RespBean.error("Failed to Added!!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteDepById(@PathVariable Integer id){
        Department dep = new Department();
        dep.setId(id);
        departmentService.deleteDepById(dep);
        if (dep.getResult() == -2) {
            return RespBean.error("This Department contains depts, Failed to delete!!!");
        } else if (dep.getResult() == -1) {
            return RespBean.error("This Department contains emps, Failed to delete!!!");
        } else if (dep.getResult() == 1) {
            return RespBean.ok("Successfully delete!!");
        }
        return RespBean.error("Failed to Delete!!");
    }
}
