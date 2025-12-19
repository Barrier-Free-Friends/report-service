package org.bf.reportservice.domain.repository;

import org.bf.reportservice.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * 제보 엔티티 저장소
 */
public interface ReportRepository extends JpaRepository<Report, UUID> {
}
