package com.example.nam21.Service;

import com.example.nam21.DB.DAO.CommentDAO;
import com.example.nam21.DB.DTO.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;


    public int submit(String userID, String boardID, String commentContent) {

        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("userID",userID);
        map.put("boardID",boardID);
        map.put("commentContent",commentContent);

        try {

            return commentDAO.submit(map);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;

    }


    public ArrayList<CommentDTO> getCommentListByID(String boardID, String commentID) {

        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("boardID",boardID);
        map.put("commentID",Integer.parseInt(commentID));
        ArrayList<CommentDTO> commentList = new ArrayList<CommentDTO>();

        try {
            List<CommentDTO> comment = commentDAO.getCommentListByID(map);

            Iterator<CommentDTO> iterator = comment.iterator();

            while(iterator.hasNext()) {
                CommentDTO commentDto = iterator.next();
                commentDto.setCommentContent(commentDto.getCommentContent().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                int commentTime = Integer.parseInt(commentDto.getCommentTime().substring(11,13));
                String timeType = "오전";
                if(commentTime >= 12) {
                    timeType = "오후";
                    commentTime -= 12;
                }
                commentDto.setCommentTime(commentDto.getCommentTime().substring(0,11)+" "+timeType+" "+commentTime+":"+commentDto.getCommentTime().substring(14,16)+"");
                commentList.add(commentDto);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return commentList;

    }


    public ArrayList<CommentDTO> getCommentListByRecent(String boardID, String commentID) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("boardID",boardID);
        map.put("commentID",Integer.parseInt(commentID));
        ArrayList<CommentDTO> commentList = new ArrayList<CommentDTO>();

        try {
            List<CommentDTO> comment = commentDAO.getCommentListByRecent(map);

            Iterator<CommentDTO> iterator = comment.iterator();

            while(iterator.hasNext()) {
                CommentDTO commentDto = iterator.next();
                commentDto.setCommentContent(commentDto.getCommentContent().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                int commentTime = Integer.parseInt(commentDto.getCommentTime().substring(11,13));
                String timeType = "오전";
                if(commentTime >= 12) {
                    timeType = "오후";
                    commentTime -= 12;
                }
                commentDto.setCommentTime(commentDto.getCommentTime().substring(0,11)+" "+timeType+" "+commentTime+":"+commentDto.getCommentTime().substring(14,16)+"");
                commentList.add(commentDto);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return commentList;
    }


    public int commentCount(int boardID) {
        try {

            int count = commentDAO.commentCount(boardID);
            if(count != 0) {
                return count;
            }else {
                return 0;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
