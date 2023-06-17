package com.blue.bluearchive.report.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

@Data
public class ReportdDto {
    private int target;
    private String targetTitle;
    private String targetContent;
    private String targetCreatedBy;
    private String targetNickName;
    private String reportContent;
    private String reportCategory;
    private String nickName;
}
