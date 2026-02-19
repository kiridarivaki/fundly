package com.fundly.domain.report.core.port.in;

import java.util.Map;

public interface ReportGenerationService {
    byte[] generateReport(String templateName, Map<String, Object> parameters);
}
