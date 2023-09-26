package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer>{

}
