package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Visitor, Integer> {

}