package com.tcit.vms.vms.service;

import com.tcit.vms.vms.model.VisitorType;
import com.tcit.vms.vms.repository.VisitorTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VisitorTypeService {
    @Autowired
    private VisitorTypeRepository visitorTypeRepository;

    public List<VisitorType> getVisitorTypeList() {
        List<VisitorType> visitorTypeList= visitorTypeRepository.findAll();
            return visitorTypeList.stream().filter(s-> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
    }
    public VisitorType getvisitorTypeById(Integer id) throws RuntimeException {
        return visitorTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("No VisitorType found for ID " + id));
    }
    public VisitorType createVisitorType(VisitorType visitorType) {
        return visitorTypeRepository.save(visitorType);
    }
    public VisitorType updateVisitorType(VisitorType visitorType) {
        return visitorTypeRepository.save(visitorType);
    }
    public VisitorType getVisitorTypeById(Integer visitorTypeId)
    {
        return  visitorTypeRepository.findById(visitorTypeId).get();
    }
    public VisitorType deleteVisitorType(Integer id) {
        VisitorType visitorType=getVisitorTypeById(id);
        visitorType.setActive(false);
        return visitorTypeRepository.save(visitorType);
    }
}



