package com.blue.bluearchive.admin.service;

import com.blue.bluearchive.admin.dto.AdminBoardDto;
import com.blue.bluearchive.admin.entity.Category;
import com.blue.bluearchive.admin.repository.CategoryRepository;
import com.blue.bluearchive.board.dto.BoardDto;
import com.blue.bluearchive.board.entity.Board;
import com.blue.bluearchive.board.repository.BoardRepository;
import com.blue.bluearchive.report.repository.ReportBoardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminBoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final ReportBoardRepository reportBoardRepository;
    public List<AdminBoardDto> getAllBoards() {
        List<Board> boardEntities = boardRepository.findAll();
        List<AdminBoardDto> boardDtos = new ArrayList<>();
        for (Board board : boardEntities) {
            boardDtos.add(modelMapper.map(board, AdminBoardDto.class));
            int size = reportBoardRepository.findByBoardBoardIdAndReportStatusFalse(board.getBoardId()).size();
            boardDtos.get(boardDtos.size()-1).setNotReportCount(size);
            System.out.println("boardDtos = " + boardDtos);
        }
        return boardDtos;
    }

    public List<AdminBoardDto> getBoardsByCategoryId(int categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        List<Board> boardEntities = boardRepository.findByCategory(category);
        List<AdminBoardDto> boardDtos = new ArrayList<>();
        for (Board board : boardEntities) {
            boardDtos.add(modelMapper.map(board, AdminBoardDto.class));
            int size = reportBoardRepository.findByBoardBoardIdAndReportStatusFalse(board.getBoardId()).size();
            boardDtos.get(boardDtos.size()-1).setNotReportCount(size);
            System.out.println("boardDtos = " + boardDtos);
        }
        return boardDtos;
    }
}
