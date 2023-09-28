package com.tcit.vms.vms.repository;


import com.tcit.vms.vms.model.Visit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Qualifier("vistior")
@Repository
@Transactional
public interface VisitRepository extends JpaRepository<Visit,Integer>,CustomVisitRepository {
    @Query("from Visit v where v.visitor.id=?1 and v.dateOfVisit=?2")
    Optional<List<Visit>> findByVisitorIdAndDateOfVisit(Integer visitorId, LocalDateTime dateOfVisit);
    Optional<Visit> findById(Integer id);
    @Modifying
    @Query(value = "update visits set approvedbyhost=?1, reasonid=?3, comments=?4  where id=?2" , nativeQuery = true)
    void updateApprovedByHost(Integer approvedbyhost,Integer id ,Integer reasonid, String comments);
    @Modifying
    @Query(value = "update visits set approvedbysecurity=?1 , reasonid=?3, comments=?4 ,approvedbysecurityid=?5 where id=?2 "  , nativeQuery = true)
    void updateApprovedBySecurity( Integer approvedbysecurity,Integer id,Integer reasonid, String comments,String approvedBySecurityId);

    /*@Modifying
    @Query(value = "update visits set approvedbysecurity=?1 , reasonid=?3, comments=?4 where id=?2 "  , nativeQuery = true)
    void updateApprovedBySecurity( Integer approvedbysecurity,Integer id,Integer reasonid, String comments);*/
     @Modifying
     @Query(value ="update visits set status='Missed' where status='Pending' and dateofvisit<?1", nativeQuery=true)
     void updateStatus(LocalDateTime dateOfVisit);
    List<Visit> findAll();
    Optional<Visit> findByIdAndIsActive(Integer id, Boolean isActive);
    @Query(value = "SELECT count(*) FROM visits v where  v.dateofvisit between ?1 and ?2 and v.approvedbyhost=1 and v.approvedbysecurity=1 and v.isactive=true",nativeQuery = true)
    public  Integer getTotalVisitCount(String startDate, String endDate);
    @Query(value = "SELECT count(*) FROM visits v where  v.dateofvisit between ?1 and ?2 and v.approvedbyhost=1 and v.approvedbysecurity=1 and v.isactive=true   and status='pending' ",nativeQuery = true)
    public  Integer getVisitPendingCount(String startDate, String endDate);
    @Query(value = "SELECT count(*) FROM visits v where  v.dateofvisit between ?1 and ?2 and v.approvedbyhost=1 and v.approvedbysecurity=1 and v.isactive=true and status='checkedIn'",nativeQuery = true)
    public  Integer getVisitCheckedInCount(String startDate, String endDate);
    @Query(value = "SELECT count(*) FROM visits v where  v.dateofvisit between ?1 and ?2 and v.approvedbyhost=1 and v.approvedbysecurity=1 and v.isactive=true and status='verified'",nativeQuery = true)
    public  Integer getVisitVerifiedCount(String startDate, String endDate);
    @Query(value = "SELECT count(*) FROM visits v where  v.dateofvisit between ?1 and ?2 and v.approvedbyhost=1 and v.approvedbysecurity=1 and v.isactive=true and status='completed'",nativeQuery = true)
    public  Integer getVisitCompletedCount(String startDate, String endDate);
    @Query(value ="SELECT count(*) FROM visits v where  v.dateofvisit <?2  and   v.dateofvisit >?1 and v.status='missed' and v.isactive=true and v.approvedbyhost=1 and v.approvedbysecurity=1",nativeQuery = true)
    public  Integer getVisitMissedCount(String startDate, String endDates);
    Optional<Visit> findFirstByVisitorIdOrderByCreatedDateDesc (Integer id);
    List<Visit> findByStaffId(Integer id);
    @Query("from Visit  where  isActive=true and Status = ?1 and isBioMetricVerified = ?2")
    List<Visit> findByStatusAndIsBioMetricVerified(String status,Boolean isBioMetricVerified);
    @Query("from Visit where isActive=true and dateOfVisit between ?1 and ?2 and status=?3 and approvedByHost=1 and approvedBySecurity=1 ")
    List<Visit> findAllVisitsWithFiltersForTab(LocalDateTime from, LocalDateTime to, String status);
    @Query("from Visit  where isActive=true and dateOfVisit between ?1 and ?2 and status=?3 and approvedbyhost=1 and approvedbysecurity=0 ")
    List<Visit> findAllVisitorsWithFiltersForSecurityApproval(LocalDateTime from, LocalDateTime to,String status);
    @Modifying
    @Query(value = "update visits v set isActive=false where visitorid=?1 " , nativeQuery = true)
    void updateInActive(Integer id);

}
