package com.fundly.domain.report.infrastructure.service;

import com.fundly.common.exception.ReportGenerationException;
import com.fundly.domain.report.core.port.in.ReportGenerationService;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
@Log4j2
public class ReportGenerationServiceImpl implements ReportGenerationService {
    private static final String REPORT_BASE_PATH = "/report-templates/";

    @Override
    public byte[] generateReport(String templateName, Map<String, Object> parameters) {
        try {
            JasperReport template = loadJasperTemplate(templateName);

            JasperPrint jasperPrint = JasperFillManager.fillReport(template, parameters);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception ex) {
            log.warn("Failed to generate Jasper report. With exception: {}", ex.getMessage());
            throw new ReportGenerationException("Failed to execute Jasper report generation.");
        }
    }

    @Cacheable(value = "jasperTemplates", key = "#templateName")
    public JasperReport loadJasperTemplate(String templateName) throws Exception {
        final String JRXML_PATH = REPORT_BASE_PATH + templateName + ".jrxml";

        try (InputStream reportStream = new ClassPathResource(JRXML_PATH).getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(reportStream);

            log.info("Successfully compiled report template {}.", JRXML_PATH);

            return report;
        } catch (Exception ex) {
            log.error("Failed to compile report template {}", templateName, ex);
            throw ex;
        }
    }
}
