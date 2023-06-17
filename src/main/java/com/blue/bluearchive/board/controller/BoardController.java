package com.blue.bluearchive.board.controller;

import com.blue.bluearchive.admin.service.CategoryService;
import com.blue.bluearchive.admin.dto.CategoryDto;
import com.blue.bluearchive.admin.entity.Category;
import com.blue.bluearchive.board.dto.*;
import com.blue.bluearchive.board.repository.BoardRepository;
import com.blue.bluearchive.board.service.*;
import com.blue.bluearchive.member.dto.CustomUserDetails;
import com.blue.bluearchive.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final CategoryService categoryService;

    private final CommentService commentService;

    private final CommentsCommentService commentsCommentService;

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final BoardLikeHateService boardLikeHateService;
//    private String[] boardImgFiles; // boardImgFiles 필드


    private void processCommentCounts(List<BoardDto> boardList) {
        for (BoardDto boardDto : boardList) {
            int boardId = boardDto.getBoardId();

            List<CommentDto> commentDtoList = commentService.getCommentByBoardId(boardId);
            Map<Integer, List<CommentsCommentDto>> commentsCommentMap = new HashMap<>();

            int totalCommentCount = 0;

            for (CommentDto commentDto : commentDtoList) {
                int commentId = commentDto.getCommentId();
                List<CommentsCommentDto> commentsCommentDtoList = commentsCommentService.getCommentsCommentByCommentId(commentId);
                commentsCommentMap.put(commentId, commentsCommentDtoList);

                totalCommentCount += commentsCommentDtoList.size();
            }

            int commentCount = commentDtoList.size() + totalCommentCount;
            boardDto.setCommentCount(commentCount);
            boardService.updateCommentCount(boardDto);
        }
    }
    @GetMapping(value = "/board/all")
    public String getBoardList(Model model) {
        List<BoardDto> boardList = boardService.getAllBoards();
        processCommentCounts(boardList);
        model.addAttribute("boardList", boardList);
        List<CategoryDto> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);

        return "board/list";
    }


    // POST 방식으로 "/board/{categoryId}" 경로에 접근할 때의 처리
    @GetMapping(value = "/board/{categoryId}")
    public String getBoardsByCategory(@PathVariable int categoryId, Model model) {
        Category category = categoryService.getCategoryById(categoryId); // categoryId에 해당하는 카테고리를 가져옵니다.
        List<BoardDto> boardList = boardService.getBoardsByCategory(category); // 해당 카테고리에 속한 게시물들을 가져옵니다.
        model.addAttribute("boardList", boardList); // boardList를 모델에 추가합니다.
        processCommentCounts(boardList);


        List<CategoryDto> categoryList = categoryService.getAllCategory(); // 모든 카테고리를 가져옵니다.
        model.addAttribute("categoryList", categoryList); // categoryList를 모델에 추가합니다.


        return "board/list"; // board/list 뷰를 반환합니다.
    }
    @GetMapping("/board/Detail/{boardId}")
    public String getBoardDetails(@PathVariable int boardId, Model model) {
        boardService.incrementBoardCount(boardId);
        BoardDto board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);
        BoardFormDto boardFormDto= boardService.getBoardImgById(boardId);
        model.addAttribute("boardFormDto",boardFormDto);
        List<CommentDto> commentList = commentService.getCommentByBoardId(boardId);
        model.addAttribute("commentList", commentList);
        Map<Integer, List<CommentsCommentDto>> commentsCommentMap = new HashMap<>();
        for (CommentDto comment : commentList) {
            int commentId = comment.getCommentId();
            List<CommentsCommentDto> commentsCommentList = commentsCommentService.getCommentsCommentByCommentId(commentId);
            commentsCommentMap.put(commentId, commentsCommentList);
        }
        model.addAttribute("commentsCommentMap", commentsCommentMap);

        return "board/boardDetail_HAN";
    }
    @GetMapping(value = "/board/Write/new")
    public String getBoardWrite(Model model){
        List<CategoryDto> categoryList = categoryService.getAllCategory();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("boardFormDto",new BoardFormDto());
        return "board/boardWrite3";
    }
    @PostMapping(value = "/board/Write/new")
    public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult, Model model
            ,@RequestParam(value = "boardImgFile" ,required = false) List<MultipartFile> boardImgFileList){

        if(bindingResult.hasErrors()){
            model.addAttribute("errorMessage","게시글 등록 중 오류");
            return "/board/boardWrite3";
        }
            try {
                boardService.saveBoard(boardFormDto, boardImgFileList);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "게시글 등록 중 오류");
                System.out.println("저기 오류");
            }

        return "redirect:/board/"+boardFormDto.getCategory().getCategoryId();
    }
    @GetMapping(value = "board/Edit/{boardId}")
    public String boardEditForm(@PathVariable("boardId")int boardId, Model model){
        try{
            List<CategoryDto> categoryList = categoryService.getAllCategory();
            model.addAttribute("categoryList", categoryList);

            BoardFormDto boardFormDto= boardService.getBoardImgById(boardId);
            model.addAttribute("boardFormDto",boardFormDto);

            List<BoardImgDto> imageList = boardFormDto.getBoardImgDtoList(); // 이미지 목록을 가져온다고 가정합니다.
            List<String> imageUrls = new ArrayList<>();
            for (BoardImgDto image : imageList) {
                imageUrls.add(image.getBoardImgUrl()); // 이미지 URL 정보를 가져온다고 가정합니다.
            }
            model.addAttribute("imageUrls", imageUrls); // 이미지 URL 목록을 모델에 추가하여 View로 전달합니다.



        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","오류");
            return  "redirect:/board/Detail/"+boardId;
        }
        return "/board/boardWrite3";
    }
    @PostMapping(value = "/board/Edit/{boardId}")
    public String boardUpdate(BoardFormDto boardFormDto,BindingResult bindingResult
            ,@RequestParam(value = "boardImgFile",required = false)List<MultipartFile>boardImgFileList,@RequestParam(value = "boardImgUrl",required = false)List<String>boardImgUrlList,
                              Model model){
        System.out.println(boardFormDto);
        System.out.println(boardImgFileList);
        System.out.println(boardImgUrlList);
        System.out.println("왔냐");
        if(bindingResult.hasErrors()){
            return "board/boardWrite3";
        }
        try{
            boardService.updateBoard(boardFormDto,boardImgUrlList,boardImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","게시글 수정 중 에러 발생");
            return "board/boardWrite3";
        }
       return "redirect:/board/Detail/"+boardFormDto.getBoardId();
    }
    @PostMapping("/boardlikeHate")
    @ResponseBody
    public BoardLikeHateDto handleBoardLikeHateRequest(@RequestBody BoardLikeHateDto boardLikeHateDto) {
        if (boardLikeHateDto.isHate()) {
            boardLikeHateService.hateBoard(boardLikeHateDto.getBoardId(), boardLikeHateDto.getIdx());
        } else if (boardLikeHateDto.isLike()) {
            boardLikeHateService.likeBoard(boardLikeHateDto.getBoardId(), boardLikeHateDto.getIdx());
        }
        return new BoardLikeHateDto(
                boardService.getBoardLikeCount(boardLikeHateDto.getBoardId()),
                boardService.getBoardHateCount(boardLikeHateDto.getBoardId())
        );
    }


    @RequestMapping(value = "/board/Write/url",method = RequestMethod.POST)
    public String getData(Model model, MultipartHttpServletRequest req){

        //get image file.
        List<MultipartFile> multipartFileList = new ArrayList<>();
        try{
            MultiValueMap<String, MultipartFile> files = req.getMultiFileMap();
            for (Map.Entry<String, List<MultipartFile>> entry : files.entrySet()) {
                List<MultipartFile> fileList = entry.getValue();
                for (MultipartFile file : fileList) {
                    if (file.isEmpty()) continue;
                    multipartFileList.add(file);
                }
            }

            if(multipartFileList.size()>0) {
                for(MultipartFile file: multipartFileList) {
                    file.transferTo(new File("D:/storage/board" + File.separator + file.getOriginalFilename()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        model.addAttribute("log","사진 "+multipartFileList.size()+"장 전송완료!");
        return "/board/boardWrite:: #resultDiv";
    }


}
