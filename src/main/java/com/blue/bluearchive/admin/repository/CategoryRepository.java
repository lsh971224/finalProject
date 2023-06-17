package com.blue.bluearchive.admin.repository;

import com.blue.bluearchive.admin.entity.Category;
import com.blue.bluearchive.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // 추가적인 쿼리 메소드 선언 가능

    //승훈이 추가
    boolean existsByCategoryNameIgnoreCase(String categoryName);

}
