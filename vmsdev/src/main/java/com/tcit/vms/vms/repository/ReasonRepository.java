package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.model.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
    @Repository
    public interface  ReasonRepository  extends JpaRepository<Reason, Integer>   {

}
