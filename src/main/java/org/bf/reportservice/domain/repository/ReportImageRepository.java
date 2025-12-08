package org.bf.reportservice.domain.repository;

import org.bf.reportservice.domain.entity.ReportImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportImageRepository extends JpaRepository<ReportImage, UUID> {
}
