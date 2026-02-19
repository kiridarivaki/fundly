package com.fundly.domain.report.infrastructure.service;

import com.fundly.common.dto.UploadDTO;
import com.fundly.common.exception.ReportGenerationException;
import com.fundly.common.port.in.BlobService;
import com.fundly.domain.auth.core.port.in.SecurityUserService;
import com.fundly.domain.expense.core.port.out.ExpenseCategoryRepository;
import com.fundly.domain.expense.core.port.out.ExpenseRepository;
import com.fundly.domain.report.adapter.web.dto.GenerateReportRequest;
import com.fundly.domain.report.core.model.Report;
import com.fundly.domain.report.core.model.enums.ReportStatus;
import com.fundly.domain.report.core.port.in.ReportGenerationService;
import com.fundly.domain.report.core.port.in.ReportService;
import com.fundly.domain.report.core.port.out.ReportRepository;
import com.fundly.domain.report.infrastructure.dto.CategorySummaryDTO;
import com.fundly.domain.report.infrastructure.dto.ExpensesTrendDTO;
import com.fundly.domain.report.infrastructure.mapper.ReportMapper;
import com.fundly.domain.user.infrastructure.dto.UserDTO;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ReportServiceImpl implements ReportService {
    private final SecurityUserService securityUserService;
    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;
    private final ReportRepository reportRepository;
    private final BlobService blobService;
    private final ReportGenerationService reportGenerationService;
    private final ReportMapper mapper;

    public ReportServiceImpl(
            SecurityUserService securityUserService,
            ExpenseRepository expenseRepository,
            ExpenseCategoryRepository categoryRepository,
            ReportRepository reportRepository,
            BlobService blobService,
            ReportGenerationService reportGenerationService,
            ReportMapper mapper) {
        this.securityUserService = securityUserService;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.reportRepository = reportRepository;
        this.blobService = blobService;
        this.reportGenerationService = reportGenerationService;
        this.mapper = mapper;
    }

    @Override
    public void createReport(GenerateReportRequest reportRequest) {

        Report report = mapper.toEntity(reportRequest);
        UserDTO userDto = securityUserService.getLoggedInUserInfo();

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("type", report.getType().name());
        parameters.put("reportPeriod", report.getStartDate() + " to " + report.getEndDate());
        parameters.put("firstName", userDto.getFirstName());
        parameters.put("lastName", userDto.getLastName());

        BigDecimal totalAmount = expenseRepository.calculateTotalAmountSpentForPeriod(
                userDto.getId(), report.getStartDate(), report.getEndDate());
        
        parameters.put("totalAmount", totalAmount);

        List<CategorySummaryDTO> categorySummaries = categoryRepository.findCategorySummaryByUserIdAndDateRange(
                userDto.getId(), report.getStartDate(), report.getEndDate());
        List<ExpensesTrendDTO> expensesTrends = expenseRepository.findTimePeriodTrendByUserIdAndDateRange(
                userDto.getId(), report.getStartDate(), report.getEndDate());

        parameters.put("categorySummaryDataSource", new JRBeanCollectionDataSource(categorySummaries));
        parameters.put("trendDataSource", new JRBeanCollectionDataSource(expensesTrends));

        try {
            byte[] pdfBytes = reportGenerationService.generateReport(
                    "expenses-report",
                    parameters
            );

            save(report, pdfBytes);

            log.info("Successfully initiated summary report for user {}.", userDto.getEmail());

        } catch (ReportGenerationException e) {
            throw new RuntimeException("Error during report file generation.", e);
        }
    }

    private void save(Report report, byte[] fileBytes) {
        String fileName = report.getName() + report.getId();

        UploadDTO uploadDto = new UploadDTO(fileName, "reports", "application/pdf", fileBytes);

        String reportUrl = blobService.upload(uploadDto);

        report.setFileUrl(reportUrl);
        report.setStatus(ReportStatus.FINISHED);

        reportRepository.save(report);
    }
}
