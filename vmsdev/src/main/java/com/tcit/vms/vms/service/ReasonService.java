package com.tcit.vms.vms.service;

import com.tcit.vms.vms.dto.request.ReasonRequestDto;
import com.tcit.vms.vms.model.Reason;
import com.tcit.vms.vms.repository.ReasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ReasonService {
    @Autowired
    private ReasonRepository reasonRepository;
    public List<Reason> getReasonList() {
        List<Reason> reasonList= reasonRepository.findAll();
        return reasonList.stream().filter(s-> Objects.nonNull(s.isActive()) && s.isActive()).collect(Collectors.toList());
    }
    public Reason createReason(ReasonRequestDto reasonRequestDto) throws IOException {
        log.info("ReasonService request # {}", reasonRequestDto);
        Reason reason = new Reason();
        reason.setId(reasonRequestDto.getId());
        reason.setReasonName(reasonRequestDto.getReasonName());
        reason.setCreatedDate(LocalDateTime.now());
        reason.setActive(true);
        Reason reasonDb = reasonRepository.save(reason);
        return reasonDb;
    }
    public Reason getReasonById(Integer id) throws RuntimeException {
        return reasonRepository.findById(id).orElseThrow(() -> new RuntimeException("No Reason found for ID " + id));
    }
        public Reason deleteReason(Integer id) {
        Reason reason1 = getReasonById(id);
        reason1.setActive(false);
        return reasonRepository.save(reason1);
    }
}
