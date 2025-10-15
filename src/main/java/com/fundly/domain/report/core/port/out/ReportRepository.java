package com.fundly.domain.report.core.port.out;

import com.fundly.domain.report.core.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
}
