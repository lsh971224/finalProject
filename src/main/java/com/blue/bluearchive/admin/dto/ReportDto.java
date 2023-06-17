package com.blue.bluearchive.admin.dto;

import com.blue.bluearchive.board.entity.Board;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {
    private int reportId;
    private String createdBy;
    private String reportCategory;
    private LocalDateTime regTime;
    private String reportContent;
    private Board board;
    private boolean reportStatus;
}
