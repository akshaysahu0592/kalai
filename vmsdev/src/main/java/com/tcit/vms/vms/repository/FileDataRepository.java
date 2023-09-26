package com.tcit.vms.vms.repository;
import com.tcit.vms.vms.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface FileDataRepository extends JpaRepository<FileData,Integer> {
    Optional<FileData> findByName(String fileName);
}

