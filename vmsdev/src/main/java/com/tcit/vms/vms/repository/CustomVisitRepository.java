package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.dto.request.SearchVisitRequestDto;
import com.tcit.vms.vms.dto.request.SearchVisitRequestHistoryDto;
import com.tcit.vms.vms.model.Visit;
import java.util.List;
public interface CustomVisitRepository  {
    List<Visit> findAllVisitsWithFilters(SearchVisitRequestDto dto);
    List<Visit> findAllVisitsWithFiltersHistory(SearchVisitRequestHistoryDto dto);

}
