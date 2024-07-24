package com.study.board.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    //게시글 번호
    private String title;
    //제목
    private  String content;
    //내용
    private String filename;
    //파일 이름
    private String filepath;
    //파일 경로
}

//이렇게 entity와 관련된 부분을 jpa가 읽어들이는 것이다.
