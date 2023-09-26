package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByEmail(String email);

    List<Staff> findByDepartmentId(Integer departmentId);
    Optional<Staff> findById(Integer id);
    Optional<Staff> findByIdAndIsActive(Integer id, Boolean isActive);
    List<Staff> findByRoleId(int i);

}