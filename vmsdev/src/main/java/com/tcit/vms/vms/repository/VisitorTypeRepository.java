package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.model.VisitorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VisitorTypeRepository extends JpaRepository<VisitorType, Integer> {

}
