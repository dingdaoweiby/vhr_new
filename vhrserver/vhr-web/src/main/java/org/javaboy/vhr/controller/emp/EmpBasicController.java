package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.model.*;
import org.javaboy.vhr.service.*;
import org.javaboy.vhr.utils.PoiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    NationService nationService;

    @Autowired
    PoliticsstatusService politicsstatusService;

    @Autowired
    JobLevelService jobLevelService;

    @Autowired
    PositionService positionService;

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          Employee employee, Date[] beginDateScope) {
        return employeeService.getEmployeeByPage(page, size, employee,beginDateScope);
    }

    @PostMapping("/")
    public RespBean addEmployee(@RequestBody Employee employee) {
        if (employeeService.addEmployee(employee) == 1) {
            return RespBean.ok("Successfully Added!!!");
        }
        return RespBean.error("Failed to Add!!");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteEmpById(@PathVariable Integer id) {
        if (employeeService.deleteEmpById(id) == 1) {
            return RespBean.ok("Successfully Delete Emp");
        }
        return RespBean.error("Failed to Delete Emp!!!");
    }

    @PutMapping("/")
    public RespBean updateEmp(@RequestBody Employee employee) {
        if (employeeService.updateEmp(employee) == 1) {
            return RespBean.ok("Successfully Update!!");
        }
        return RespBean.error("Failed to Update!!");
    }

    @GetMapping("/nations")
    public List<Nation> getAllNations() {
        return nationService.getAllNations();
    }

    @GetMapping("/politicsstatus")
    public List<Politicsstatus> getAllPoliticsstatus() {
        return politicsstatusService.getAllPoliticsstatus();
    }

    @GetMapping("/joblevels")
    public List<JobLevel> getAllJobLevels() {
        return jobLevelService.getAllJobLevels();
    }

    @GetMapping("/positions")
    public List<Position> getAllPosition() {
        return positionService.getAllPositions();
    }

    @GetMapping("/maxWorkID")
    public RespBean maxWorkID() {
        RespBean bean = RespBean.build().setStatus(200).setObj(String.format("%08d",
                employeeService.maxWorkID() + 1));
        return bean;
    }

    @GetMapping("/deps")
    public List<Department> getAllDeps() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData() {
        List<Employee> list = (List<Employee>) employeeService.getEmployeeByPage(null, null, null,null).getData();
        return PoiUtils.employee2Excel(list);
    }

    @PostMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        // To check the upload function
        // file.transferTo(new File("C:\\Users\\dingd\\javaboy.xls"));
        List<Employee> list = PoiUtils.excel2Employee(file, nationService.getAllNations(),
                politicsstatusService.getAllPoliticsstatus(),departmentService.getAllDepartmentsWithoutChildren(),
                positionService.getAllPositions(),jobLevelService.getAllJobLevels());
        // To check the uploaded file can be received
        /*for (Employee employee : list) {
            System.out.println(employee);
        }*/
        if (employeeService.addEmps(list) == list.size()) {
            return RespBean.ok("Successfully Upload!!");
        }
        return RespBean.error("Failed to upload!!");
    }

}
