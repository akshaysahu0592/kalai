package com.tcit.vms.vms.controller;

import com.tcit.vms.vms.dto.response.DepartmentResponseDto;
import com.tcit.vms.vms.dto.response.ResponseDto;
import com.tcit.vms.vms.model.Department;
import com.tcit.vms.vms.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @GetMapping("/GetAllDepartmentList")
    public List<DepartmentResponseDto> getDepartmentList() {
        List<Department> departmentList=departmentService.getDepartmentList();
        return departmentList.stream().map(e-> DepartmentResponseDto.builder(e)).collect(Collectors.toList());
    }
    @PostMapping("/Department")
    public ResponseDto createDepartment(@RequestBody Department department){
        departmentService.createDepartment(department);
        return new ResponseDto("SUCCESS", "Department Created","");
    }
    @PutMapping("/Department")
    public ResponseDto updateDepartment(@RequestBody Department department){
        departmentService.updateDepartment(department);
        return new ResponseDto("SUCCESS", "Department Updated","");
    }
    @DeleteMapping("/Department")
    public ResponseDto deleteDepartment(@RequestBody Department department)
    {
        departmentService.deleteDepartment(department.getId());
        return new ResponseDto("SUCCESS", "Department Deleted","");
    }
}
