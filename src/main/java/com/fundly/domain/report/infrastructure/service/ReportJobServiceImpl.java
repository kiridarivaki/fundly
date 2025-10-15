package com.fundly.domain.report.infrastructure.service;

import com.fundly.domain.report.adapter.web.dto.GenerateReportRequest;
import com.fundly.domain.report.core.model.Report;
import com.fundly.domain.report.core.model.enums.ReportStatus;
import com.fundly.domain.report.core.port.in.ReportJobService;
import com.fundly.domain.report.core.port.in.ReportService;
import com.fundly.domain.report.core.port.out.ReportRepository;
import com.fundly.domain.report.infrastructure.mapper.ReportMapper;
import lombok.extern.log4j.Log4j2;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ReportJobServiceImpl implements ReportJobService {
    private final BackgroundJob jobClient;
    private final ReportService reportService;
    private final ReportRepository reportRepository;
    private final ReportMapper mapper;

    public ReportJobServiceImpl(
            BackgroundJob jobClient,
            ReportService reportService,
            ReportRepository reportRepository,
            ReportMapper mapper) {
        this.jobClient = jobClient;
        this.reportService = reportService;
        this.reportRepository = reportRepository;
        this.mapper = mapper;
    }

    @Override
    public void submitGenerateReportJob(GenerateReportRequest reportRequest) {
        reportRequest.setStatus(ReportStatus.PENDING);
        Report report = reportRepository.save(mapper.toEntity(reportRequest));

        try {
            BackgroundJob.enqueue(() -> reportService.createReport(reportRequest));
        } catch (Exception ex) {
            log.error("Failed to submit report job for id {}.", report.getId());

            report.setStatus(ReportStatus.FAILED);
            reportRepository.save(report);
        }
    }
}
