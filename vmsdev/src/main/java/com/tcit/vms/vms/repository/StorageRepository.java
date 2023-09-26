package com.tcit.vms.vms.repository;

import com.tcit.vms.vms.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String fileName);
}
