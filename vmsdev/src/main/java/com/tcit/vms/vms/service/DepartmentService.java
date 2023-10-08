package com.tcit.vms.vms.service;

import com.tcit.vms.vms.exception.UserNotFoundException;
import com.tcit.vms.vms.model.Department;
import com.tcit.vms.vms.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;


   public Department getDepartmentById(Integer departmentId) throws UserNotFoundException {
        Optional <Department> userOptional = departmentRepository.findById(departmentId);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        throw new UserNotFoundException("Department not found with id "+departmentId);
    }
    public List<Department> getDepartmentList() {
            List<Department> departmentList= departmentRepository.findAll();
            return departmentList.stream().filter(s-> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
        }
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }
    public Department deleteDepartment(Integer id) {
        Department department = getDepartmentById(id);
        department.setActive(false);
        return departmentRepository.save(department);
    }
    }

