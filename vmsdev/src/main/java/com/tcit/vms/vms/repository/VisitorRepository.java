package com.tcit.vms.vms.repository;
import com.tcit.vms.vms.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface VisitorRepository extends JpaRepository <Visitor,Integer> {
    @Query(value = "SELECT * FROM visitor v join visits vv where v.id=vv.visitorid and vv.dateofvisit between ?1 and ?2",nativeQuery = true)
    List<Visitor> findByVisitingDate(String startDate, String endDate);
    @Query(value = "select * from visitor v join visits vv where v.id=vv.visitorid and vv.staffid=?1",nativeQuery = true)
    List<Visitor> findVisitorsByStaffId(Integer staffId);
    List<Visitor> findByMobileNo(String mobileNo);
    @Query("from Visitor  where  isActive=true and name like %?1%  or mobileNo like %?1% and isActive=true or  email like %?1% and isActive=true")
    List<Visitor> findAllVisitorsWithFilters(String search);
    Optional<Visitor> findByEmail(String email);
}




