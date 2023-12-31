package com.blue.bluearchive.board.dto;

import com.blue.bluearchive.admin.entity.Category;
import com.blue.bluearchive.member.entity.Member;
import com.blue.bluearchive.report.entity.Report;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDto {
    private int boardId;
    private String boardContent;
    private Integer boardCount;
    private String boardCreatedBy;
    private Integer boardHateCount;
    private Integer boardLikeCount;
    private Integer boardPreCount;
    private Integer boardReportsCount;
    private LocalDateTime regTime;
    private String boardTitle;
    private Integer commentCount;
    private Member member;
    private Category category;
}
