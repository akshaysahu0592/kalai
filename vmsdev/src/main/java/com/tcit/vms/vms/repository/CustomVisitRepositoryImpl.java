package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.dto.request.SearchVisitRequestDto;
import com.tcit.vms.vms.dto.request.SearchVisitRequestHistoryDto;
import com.tcit.vms.vms.model.Visit;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
public class CustomVisitRepositoryImpl implements CustomVisitRepository {
    @PersistenceContext
    private EntityManager em;
    @Override
    public List<Visit> findAllVisitsWithFilters(SearchVisitRequestDto dto) {
        String sql = "SELECT vv.id,\n" +
                "vv.visitorid,\n" +
                "vv.dateofvisit,\n" +
                "vv.duration,\n" +
                "vv.campusid,\n" +
                "vv.departmentid,\n" +
                "vv.staffid,\n" +
                "vv.agenda,\n" +
                "accompanycount,\n" +
                "approvedbyhost,\n" +
                "approvedbysecurity,\n" +
                "approvedbyhosttime,\n" +
                "approvedbysecuritytime,\n" +
                "checkin,\n" +
                "checkout,\n" +
                "vv.status,\n" +
                "vv.isbiometricverified,\n" +
                "vv.createddate,\n" +
                "vv.isactive, \n"+
                "vv.reasonid, \n"+
                "vv.comments, \n"+
                "vv.approvedbysecurityid FROM visitor v join visits vv where v.id=vv.visitorid and v.isActive=1 and vv.isActive=1 and vv.approvedbyhost=1 and vv.approvedbysecurity=1";
        sql = sql + " and vv.dateofvisit between '" + dto.getFromDate() + "' and '" + dto.getToDate() + "'";
        if (dto.getName() != null) {
            sql = sql + " and name like '%" + dto.getName() + "%'";
        }
        if (dto.getMobileNo() != null) {
            sql = sql + " and mobileno like '%" + dto.getMobileNo() + "%'";
        }
        if (dto.getEmail() != null) {
            sql = sql + " and email like '%" + dto.getEmail() + "%'";
        }
        log.info("sql={}", sql);
        List<Visit> result = em.createNativeQuery(sql, Visit.class).getResultList();
        return result;
    }



    /*@Override
    public List<Visit> findAllVisitsWithFiltersHistory(SearchVisitRequestHistoryDto dto) {
        String sql = "SELECT vv.id,\n" +
                "vv.visitorid,\n" +
                "vv.dateofvisit,\n" +
                "vv.duration,\n" +
                "vv.campusid,\n" +
                "vv.departmentid,\n" +
                "vv.staffid,\n" +
                "vv.agenda,\n" +
                "accompanycount,\n" +
                "approvedbyhost,\n" +
                "approvedbysecurity,\n" +
                "approvedbyhosttime,\n" +
                "approvedbysecuritytime,\n" +
                "checkin,\n" +
                "checkout,\n" +
                "vv.status,\n" +
                "vv.isbiometricverified,\n" +
                "vv.createddate,\n" +
                "vv.isactive, \n"+
                "vv.reasonid, \n"+
                "vv.comments FROM visitor v join visits vv where v.id=vv.visitorid and v.isActive=1 and vv.isActive=1 ";

        if (dto.getFromDate() != null && !dto.getFromDate().isEmpty()) {
            sql = sql + " and date(vv.dateofvisit) = '" + LocalDateTime.now().plusHours(24) + "' ";
        }
        if (dto.getToDate() != null && !dto.getToDate().isEmpty()) {
            sql = sql + " and date(vv.dateofvisit) = '" + LocalDateTime.now().plusHours(24) + "' ";
        }
        *//*if (dto.getFromDate() != null && !dto.getFromDate().isEmpty() && dto.getToDate()!=null && !dto.getToDate().isEmpty()) {
            sql = sql + " and vv.dateofvisit between '" + dto.getFromDate() + "' and '" + dto.getToDate() + "' ";
        }*//*
              if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
                  sql = sql + " and vv.status = '" + dto.getStatus() + "' ";
              }
       *//* if (dto.getSearchText() != null) {
            sql= sql + " and name like '%" + dto.getSearchText() + "%'";
        }
        if (dto.getSearchText() != null) {
            sql = sql + " and mobileno like '%" + dto.getSearchText() + "%'";
        }
        if (dto.getSearchText() != null) {
            sql = sql + " and email like '%" + dto.getSearchText() + "%')";
        }*//*

        sql=  sql + " and (v.name like '%" + dto.getSearchText() + "%'";

            sql = sql + " or v.mobileno like '%" + dto.getSearchText() + "%'";

            sql = sql + " or v.email like '%" + dto.getSearchText() + "%')";

        log.info("sql={}", sql);
        List<Visit> result = em.createNativeQuery(sql, Visit.class).getResultList();
        return result;
    }
*/
    @Override
    public List<Visit> findAllVisitsWithFiltersHistory(SearchVisitRequestHistoryDto dto) {
        String sql = "SELECT vv.id," +
                "vv.visitorid," +
                "vv.dateofvisit," +
                "vv.duration," +
                "vv.campusid," +
                "vv.departmentid," +
                "vv.staffid," +
                "vv.agenda," +
                "accompanycount," +
                "approvedbyhost," +
                "approvedbysecurity," +
                "approvedbyhosttime," +
                "approvedbysecuritytime," +
                "checkin," +
                "checkout," +
                "vv.status," +
                "vv.isbiometricverified," +
                "vv.createddate," +
                "vv.isactive, "+
                "vv.reasonid, "+
                "vv.comments, "+
                "vv.approvedbysecurityid FROM visitor v join visits vv where v.id=vv.visitorid and v.isActive=1 and vv.isActive=1 ";
        if (dto.getFromDate() != null && !dto.getFromDate().isEmpty()) {
            sql = sql + " and vv.dateofvisit >= '" + dto.getFromDate() + "'";
        }
        if (dto.getToDate() != null && !dto.getToDate().isEmpty()) {
            sql = sql + " and vv.dateofvisit <= '" + dto.getToDate() + "'";
        }

        if(dto.getStatus()!=null && !dto.getStatus().isEmpty())
        {
            sql=sql+"and  vv.status ='"+dto.getStatus()+"' ";
        }
        sql=sql+" order by vv.dateofvisit DESC";
        log.info("sql={}", sql);
        List<Visit> result = em.createNativeQuery(sql, Visit.class).getResultList();

        return result;
    }
}
