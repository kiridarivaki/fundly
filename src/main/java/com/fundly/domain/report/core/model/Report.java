package com.fundly.domain.report.core.model;

import com.fundly.common.model.BaseEntity;
import com.fundly.domain.report.core.model.enums.ReportStatus;
import com.fundly.domain.report.core.model.enums.ReportType;
import com.fundly.domain.user.core.model.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Report extends BaseEntity {
    private String name;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String filteredCategoryIds;

    private String fileUrl;

    //region mappings
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
    //endregion
}
