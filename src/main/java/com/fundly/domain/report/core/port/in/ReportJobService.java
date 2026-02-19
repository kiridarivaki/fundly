package com.fundly.domain.report.core.port.in;

import com.fundly.domain.report.adapter.web.dto.GenerateReportRequest;

public interface ReportJobService {
    void submitGenerateReportJob(GenerateReportRequest reportRequest);
}
