package com.blue.bluearchive.report.repository;

import com.blue.bluearchive.board.entity.Board;
import com.blue.bluearchive.board.entity.Comment;
import com.blue.bluearchive.board.entity.CommentsComment;
import com.blue.bluearchive.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportBoardRepository extends JpaRepository<Report,Integer> {
    List<Report> findByBoard(Board boardId); //게시판에있는 신고된것들을 다 가져오려고


    List<Report> findByComment(Comment comment);

    List<Report> findByCommentsComment(CommentsComment commentsComment);
}
