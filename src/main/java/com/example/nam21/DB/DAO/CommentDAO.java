package com.example.nam21.DB.DAO;

import com.example.nam21.DB.DTO.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;


public interface CommentDAO {

    public int submit(HashMap<String,Object> map/*String userID, String boardID, String commentContent*/);

    public ArrayList<CommentDTO> getCommentListByID(HashMap<String,Object> map/*String boardID, String commentID*/);

    public ArrayList<CommentDTO> getCommentListByRecent(HashMap<String,Object> map/*String boardID, String commentID*/);

    public int commentCount(int boardID);



}
