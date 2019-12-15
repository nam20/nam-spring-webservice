package com.example.nam21.Controller;

import com.example.nam21.DB.DTO.CommentDTO;
import com.example.nam21.Service.CommentService;
import com.example.nam21.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

@Controller
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @ResponseBody
    @PostMapping(value="/CommentList", produces="application/text;charset=utf-8")
    public String commentList(HttpServletRequest request) {


        String boardID = request.getParameter("boardID");
        String listType = request.getParameter("listType");

        if (boardID == null || boardID.equals("") || listType == null
                || listType.equals("")) {
            return "";
        }
        else {
            try {

                return getID( URLDecoder.decode(boardID, "UTF-8"), listType);
            } catch (Exception e) {
                return "";
            }
        }

    }

    public String getTen(String boardID, String commentID) {
        StringBuffer result = new StringBuffer("");
        result.append("{\"result\":[");

        ArrayList<CommentDTO> commentList = commentService.getCommentListByRecent(boardID, commentID);
        if (commentList.size() == 0)
            return "";
        for (int i = 0; i < commentList.size(); i++) {

            result.append("[{\"value\": \"" + commentList.get(i).getUserID() + "\"},");
            result.append("{\"value\": \"" + commentList.get(i).getBoardID() + "\"},");
            result.append("{\"value\": \"" + commentList.get(i).getCommentContent() + "\"},");
            result.append("{\"value\": \"" + commentList.get(i).getCommentTime() + "\"}]");
            if (i != commentList.size() - 1)
                result.append(",");
        }

        result.append("], \"last\":\"" + commentList.get(commentList.size() - 1).getCommentID() + "\"}");

        return result.toString();
    }

    public String getID(String boardID, String commentID) {
        StringBuffer result = new StringBuffer("");
        result.append("{\"result\":[");

        String userProfile = "";
        ArrayList<CommentDTO> commentList = commentService.getCommentListByID(boardID, commentID);
        if (commentList.size() == 0)
            return "";

        for (int i = 0; i < commentList.size(); i++) {
            userProfile = userService.getProfile(commentList.get(i).getUserID());
            if(commentList.get(i).getUserID().equals("null")) {
                result.append("[{\"value\": \"" + "익명" + "\"},");
            }else {
                result.append("[{\"value\": \"" + commentList.get(i).getUserID() + "\"},");
            }

            result.append("{\"value\": \"" + commentList.get(i).getBoardID() + "\"},");
            result.append("{\"value\": \"" + commentList.get(i).getCommentContent() + "\"},");
            result.append("{\"value\": \"" + commentList.get(i).getCommentTime() + "\"},");
            result.append("{\"value\": \"" + userProfile + "\"}]");
            if (i != commentList.size() - 1)
                result.append(",");
        }

        result.append("], \"last\":\"" + commentList.get(commentList.size() - 1).getCommentID() + "\"}");

        return result.toString();
    }


    @ResponseBody
    @RequestMapping(method= RequestMethod.POST,value="/CommentSubmit")
    public String commentSubmit(HttpServletRequest request) throws UnsupportedEncodingException {

        String userID = request.getParameter("userID");
        String boardID = request.getParameter("boardID");
        String commentContent = request.getParameter("commentContent");

        if(userID == null || userID.equals("") || boardID == null || boardID.equals("")
                || commentContent == null || commentContent.equals("")){
            return "0";
        }

        else {
            userID= URLDecoder.decode(userID, "UTF-8");
            boardID= URLDecoder.decode(boardID, "UTF-8");

            commentContent = URLDecoder.decode(commentContent,"UTF-8");
            return commentService.submit(userID, boardID, commentContent)+"";

        }
    }


}
