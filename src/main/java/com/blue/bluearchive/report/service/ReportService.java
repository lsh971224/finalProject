package com.blue.bluearchive.report.service;


import com.blue.bluearchive.admin.dto.ReportDto;
import com.blue.bluearchive.admin.dto.ReportPageDto;
import com.blue.bluearchive.board.dto.BoardDto;
import com.blue.bluearchive.board.entity.Board;
import com.blue.bluearchive.board.entity.Comment;
import com.blue.bluearchive.board.entity.CommentsComment;
import com.blue.bluearchive.board.repository.BoardRepository;
import com.blue.bluearchive.board.repository.CommentRepository;
import com.blue.bluearchive.board.repository.CommentsCommentRepository;
import com.blue.bluearchive.member.dto.CustomUserDetails;
import com.blue.bluearchive.member.entity.Member;
import com.blue.bluearchive.member.repository.MemberRepository;
import com.blue.bluearchive.report.dto.ReportBoardFormDto;
import com.blue.bluearchive.report.dto.ReportCommentFormDto;
import com.blue.bluearchive.report.dto.ReportCommentsCommentFormDto;
import com.blue.bluearchive.report.dto.ReportdDto;
import com.blue.bluearchive.report.entity.Report;
import com.blue.bluearchive.report.repository.ReportBoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportBoardRepository reportBoardRepository;
    private final CommentRepository commentRepository;
    private final CommentsCommentRepository commentsCommentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


    //건희 추가
    @Transactional(readOnly = false)
    public Integer saveReportBoard(ReportBoardFormDto reportBoardFormDto){
        Report reportBoard = reportBoardFormDto.createReport();
//        reportBoard.setBoardId(reportBoardFormDto.getBoardId());
//        reportBoard.setTargetCreatedBy(reportBoardFormDto.getTargetCreatedBy());
//        reportBoard.setReportContent(reportBoardFormDto.getReportBoardContent());
//        reportBoard.setReportCategory(reportBoardFormDto.getReportBoardCategory());
        reportBoardRepository.save(reportBoard);

        Board board = reportBoardFormDto.getBoard();
        board.setBoardReportsCount(board.getBoardReportsCount()+1);

        return reportBoard.getReportId();
    }

    public Map<String ,String > openReportBoard(int boardId){
        Map<String, String> reportBoard = new HashMap<>();
        Board board = boardRepository.findById(boardId).orElseThrow();
        reportBoard.put("boardTitle",board.getBoardTitle());
        reportBoard.put("board",Integer.toString(board.getBoardId()));
        reportBoard.put("targetCreatedBy",board.getCreatedBy());

        Member member = memberRepository.findById(Long.valueOf(board.getCreatedBy())).orElseThrow();
        reportBoard.put("boardNickName",member.getNickName());

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;

        reportBoard.put("nickName",userDetails.getNickName());
        reportBoard.put("createdBy",Long.toString(userDetails.getIdx()));
        return reportBoard;
    }


    @Transactional(readOnly = false)
    public Integer saveReportComment(ReportCommentFormDto reportCommentFormDto){
        Report reportComment = reportCommentFormDto.createReport();
        reportBoardRepository.save(reportComment);

        Comment comment = reportCommentFormDto.getComment();
        comment.setCommentReportsCount(comment.getCommentReportsCount()+1);
//        Board board = reportCommentFormDto.getComment().getBoard();
//        board.setBoardReportsCount(board.getBoardReportsCount()+1);


        return reportComment.getReportId();
    }

    public ReportdDto openReportComment(int commentId){
        ReportdDto report = new ReportdDto();
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        report.setTargetTitle(comment.getBoard().getBoardTitle());
        report.setTargetContent(comment.getCommentContent());
        report.setTarget(commentId);
        report.setTargetCreatedBy(comment.getCreatedBy());

        Member member = memberRepository.findById(Long.valueOf(comment.getCreatedBy())).orElseThrow();
        report.setTargetNickName(member.getNickName());

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;

        report.setNickName(userDetails.getNickName());
        return report;
    }

    public ReportdDto openReportCommentsComment(int commentsCommentId){
        ReportdDto report = new ReportdDto();
        CommentsComment comment = commentsCommentRepository.findById(commentsCommentId).orElseThrow();
        report.setTargetTitle(comment.getComment().getBoard().getBoardTitle());
        report.setTargetContent(comment.getCommentsCommentContent());
        report.setTarget(commentsCommentId);
        report.setTargetCreatedBy(comment.getCreatedBy());

        Member member = memberRepository.findById(Long.valueOf(comment.getCreatedBy())).orElseThrow();
        report.setTargetNickName(member.getNickName());

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;

        report.setNickName(userDetails.getNickName());
        return report;
    }

    @Transactional(readOnly = false)
    public Integer saveReportCommentsComment(ReportCommentsCommentFormDto reportCommentsCommentFormDto){
        Report reportComment = reportCommentsCommentFormDto.createReport();
        reportBoardRepository.save(reportComment);

        CommentsComment commentsComment = reportCommentsCommentFormDto.getCommentsComment();
        commentsComment.setCommentsCommentReportsCount(commentsComment.getCommentsCommentReportsCount()+1);
//        Board board = reportCommentsCommentFormDto.getCommentsComment().getComment().getBoard();
//        board.setBoardReportsCount(board.getBoardReportsCount()+1);

        return reportComment.getReportId();
    }

    //승훈 추가
    ModelMapper modelMapper = new ModelMapper();
    public ReportPageDto getReportsForBoard(int boardId, int page, int pageSize) {
        Optional<Board> board = boardRepository.findById(boardId);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("regTime").descending());
        Page<Report> reportPage = reportBoardRepository.findByBoardAndReportStatusOrderByRegTimeDesc(board.get(), false, pageable);
        List<ReportDto> reportDtoList = reportPage.map(report -> modelMapper.map(report, ReportDto.class)).getContent();

        ReportPageDto reportPageDto = new ReportPageDto();
        reportPageDto.setReportList(reportDtoList);
        reportPageDto.setCurrentPage(reportPage.getNumber() + 1);
        reportPageDto.setTotalPages(reportPage.getTotalPages());

        return reportPageDto;
    }
    //페이징 처리
public ReportPageDto getReportsForComment(int commentId,int page, int pageSize) {
    Optional<Comment> commentsComment = commentRepository.findById(commentId);
    Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("regTime").descending());
    Page<Report> reportPage = reportBoardRepository.findByCommentAndReportStatusOrderByRegTimeDesc(commentsComment.get(), false, pageable);
    List<ReportDto> reportDtoList = reportPage.map(report -> modelMapper.map(report, ReportDto.class)).getContent();

    ReportPageDto reportPageDto = new ReportPageDto();
    reportPageDto.setReportList(reportDtoList);
    reportPageDto.setCurrentPage(reportPage.getNumber() + 1);
    reportPageDto.setTotalPages(reportPage.getTotalPages());

    return reportPageDto;
}
    public ReportPageDto getReportsForCommentsComment(int commentsCommentId, int page, int pageSize) {
        Optional<CommentsComment> commentsComment = commentsCommentRepository.findById(commentsCommentId);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("regTime").descending());
        Page<Report> reportPage = reportBoardRepository.findByCommentsCommentAndReportStatusOrderByRegTimeDesc(commentsComment.get(), false, pageable);
        List<ReportDto> reportDtoList = reportPage.map(report -> modelMapper.map(report, ReportDto.class)).getContent();

        ReportPageDto reportPageDto = new ReportPageDto();
        reportPageDto.setReportList(reportDtoList);
        reportPageDto.setCurrentPage(reportPage.getNumber() + 1);
        reportPageDto.setTotalPages(reportPage.getTotalPages());

        return reportPageDto;
    }
    @Transactional
    public void confirmReports(List<Integer> reportIds) {
        List<Report> reports = reportBoardRepository.findAllById(reportIds);

        for (Report report : reports) {
            report.setReportStatus(true); // 읽음 상태로 설정
        }

        reportBoardRepository.saveAll(reports);
    }


}
