package com.blue.bluearchive.board.repository;

import com.blue.bluearchive.admin.entity.Category;
import com.blue.bluearchive.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByCategory(Category category);


    //승훈 코드 추가
    //승훈 게시물 검색 기능 추가
    List<Board> findByBoardTitleContaining(String keyword);
    List<Board> findByBoardContentContaining(String keyword);
    List<Board> findByCreatedByContaining(String keyword);

    //  게시물 카테고리 수정 부분 코드 추가

    @Modifying
    @Query("UPDATE Board b SET b.category.categoryId = :categoryId WHERE b.boardId = :boardId")
    void updateCategoryNameById(@Param("boardId") int boardId, @Param("categoryId") int categoryId);
    List<Board> findByCategoryCategoryId(int categoryId);


}
