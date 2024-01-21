package com.study.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;





@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/") //localhost:8080/board/write
    public String boardWriteForm(){

        return "boardwrite";
    }

    @PostMapping("/writePro")
    public String boardWritePro(Board board, Model model, @RequestParam(name = "file") MultipartFile file) throws Exception {

        boardService.write(board, file);

        // System.out.println("내용 : " + content);
        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/list");
        
        return "message";
    }

    @GetMapping("/list")
    public String boardList(Model model, @PageableDefault(page=0,size=10,sort="id",direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("list", boardService.boardList(pageable));

        return "boardlist";
    }

    @GetMapping("/view") // localhost:8080/view?id=1
    public String boardView(Model model, HttpServletRequest request) {

        model.addAttribute("board", boardService.boardView(Integer.parseInt(request.getParameter("id"))));
        
        // System.out.println(model);
        return "boardview";
    }

    @GetMapping("/delete")
    public String boardDelete(HttpServletRequest request) {

        boardService.boardDelete(Integer.parseInt(request.getParameter("id")));
        return "redirect:/list";
    }
    
    @GetMapping("/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, @RequestParam(name="file")MultipartFile file) throws Exception {
        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        return "redirect:/view?id={id}";
    }
    

    // ----------------------------------------------------------------
    //  테스트 중
    // ----------------------------------------------------------------
    @GetMapping("/test")
    public String test(Integer id) {
        System.out.println(id);
        return "test";
    }
    
    @PostMapping("/test/1")
    public String postTest(@RequestParam(name = "file") MultipartFile file) throws Exception{
        boardService.test(file);
        
        return "redirect:/test";
    }
}
