package com.fundly.domain.report.infrastructure.mapper;

import com.fundly.domain.report.adapter.web.dto.GenerateReportRequest;
import com.fundly.domain.report.core.model.Report;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    Report toEntity(GenerateReportRequest reportRequest);
}
