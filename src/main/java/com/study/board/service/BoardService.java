package com.study.board.service;

import com.study.board.BoardApplication;
import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        board.setFilename(fileName);
        board.setFilepath("/files/"+fileName);
        boardRepository.save(board);
    }

    //게시글 리시트 처리
    public Page<Board> boardList(Pageable pageable){
        return boardRepository.findAll(pageable);

    }

    //특정 게시글 불러오기
    public Board boardView(Integer id){
        //특정 게시글의 id를 넣어서 원하는 게시글을 구할 수 있게 구현할 수 있게 되는 것이다.

        return boardRepository.findById(id).get();
    }

    //특정 게시글 삭제
    public void boardDelete(Integer id){

        boardRepository.deleteById(id);
    }

    //검색 기능
    public Page<Board> boardSearchList(String searchkeyword,Pageable pageable){

        //이렇게 기능을 하게끔 만들어주면 되는 것이다.

        return boardRepository.findByTitleContaining(searchkeyword,pageable);
    }

}
