package com.fundly.domain.report.adapter.web.dto;

import com.fundly.domain.report.core.model.enums.ReportStatus;
import com.fundly.domain.report.core.model.enums.ReportType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenerateReportRequest {
    private String name;

    private ReportType type;

    private ReportStatus status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String filteredCategoryIds;
}
