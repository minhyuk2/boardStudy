package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;



    @GetMapping("/board/write")//localhost 8080에 연ㄹ결

    public  String boardWriteForm(){

        //여기 리턴 안에 들어가는 것은 어떤 html파일로 이동할 것인지 말해주는 것이다.

        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public  String  boardWritePro(Board board, Model model, MultipartFile file) throws  Exception{
        //entity로 만들어줬기 때문에 이렇게 그대로 받아줄 수 있게 되는 것이다.

//        System.out.println("제목 = " +board.getTitle());
//        System.out.println("내용 = " + board.getContent());
        boardService.write(board, file);
//        if(){
            model.addAttribute("message","글 작성이 완료되었습니다.");
//        }else{
//            model.addAttribute("message","글 작성이 실패되었습니다.");
//        }

        model.addAttribute("searchUrl","/board/list");
        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 10,sort = "id",direction = Sort.Direction.DESC
    ) Pageable pageable,String searchkeyword){

        Page<Board> list = null; //초기값을 설정해두는 것입니다.

        if(searchkeyword == null){
            list = boardService.boardList(pageable);
        }else{
            list = boardService.boardSearchList(searchkeyword,pageable);
        }

//        Page<Board> list = boardService.boardList(pageable);

        int nowPage = list.getPageable().getPageNumber(); //이렇게 하면 현재 페이지를 가져올 수 있게 되는 것이다.
        int startPage = Math.max(nowPage - 4,1);
        int endPage = Math.min(nowPage + 5,list.getTotalPages());


        //boardService에 넣는다.
        model.addAttribute("list",list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);


        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, @RequestParam("id") Integer id){

        model.addAttribute("board",boardService.boardView(id));

        return "boardview";
    }
    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam("id") Integer id){

        boardService.boardDelete(id);
        return "redirect:/board/list";

    }
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id , Model model){

        model.addAttribute("board",boardService.boardView(id));
        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws  Exception{

        Board boardTemp = boardService.boardView(id);//기존에 있던 것을 가져오고 새로운 것으로 덮어씌워주는 것이다.
        //이렇게 하게 되면 기존의 글을 넣어주게 된다.
        boardTemp.setTitle(board.getTitle()); //새로 작성된 데이터로 set해주는 것이다.
        boardTemp.setContent(board.getContent()); //content도 설정해준다.


        boardService.write(boardTemp, file);

        return "redirect:/board/list";
    }
}
