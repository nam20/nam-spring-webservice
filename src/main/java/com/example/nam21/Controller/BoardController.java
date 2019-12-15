package com.example.nam21.Controller;

import com.example.nam21.DB.DTO.BoardDTO;
import com.example.nam21.DB.DTO.UserDTO;
import com.example.nam21.Service.BoardService;
import com.example.nam21.Service.CommentService;
import com.example.nam21.Service.UserService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @GetMapping("/boardDelete")
    public String boardDelete(@RequestParam("boardID") String boardID, HttpServletRequest request){
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");
        //String boardID = request.getParameter("boardID");
        String userPassword = request.getParameter("userPassword");
        if(boardID == null || boardID.equals("")) {

            request.getSession().setAttribute("messageType", "오류 메시지");
            request.getSession().setAttribute("messageContent", "접근 불가");
            return "index";
        }


        BoardDTO board = boardService.getBoard(boardID);
        if(!userID.equals(board.getUserID())) {
            request.getSession().setAttribute("messageType", "오류 메시지");
            request.getSession().setAttribute("messageContent", "접근 불가");
            return "index";
        }

        String savePath = request.getRealPath("/resources/upload").replaceAll("\\\\","/");
        String prev = boardService.getRealFile(boardID);
        int result = boardService.delete(boardID);
        if(result == -1) {
            request.getSession().setAttribute("messageType", "오류 메시지");
            request.getSession().setAttribute("messageContent", "실패");
            return "index";
        }else {
            File prevFile = new File(savePath + "/" + prev);
            if(prevFile.exists()) {
                prevFile.delete();
            }
            request.getSession().setAttribute("messageType", "성공 메시지");
            request.getSession().setAttribute("messageContent", "성공");
            return "boardView";
        }
    }

    @GetMapping("/boardReply")
    public String boardReply(HttpServletRequest request, Model model, HttpSession session) {

        String userID = (String) session.getAttribute("userID");

        UserDTO user = userService.getUser(userID);
        model.addAttribute("user",user);
        return "boardReply";
    }

    @PostMapping("/BoardReply")
    public String BoardReply(HttpServletRequest request,Model model) {



        MultipartRequest multi = null;
        int fileMaxSize = 100 * 1024 * 1024;
        String savePath = request.getRealPath("/resources/upload").replaceAll("\\\\","/");
        try {
            multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
        }catch (Exception e) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","100mb 이상 불가" );
            return "index";
        }
        String userID = multi.getParameter("userID");
        HttpSession session = request.getSession();
        /*
         * UserDTO userDTO = userService.getUser(userID);
         * model.addAttribute("userDTO",userDTO);
         */

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
            return "index";
        }
        String boardID = multi.getParameter("boardID");
        if(boardID == null || boardID.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
            return "index";
        }
        String boardTitle = multi.getParameter("boardTitle");
        String boardContent = multi.getParameter("boardContent");
        if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","내용을 모두 채워주세요");
            return "boardReply";
        }

        String boardFile = "";
        String boardRealFile = "";
        File file = multi.getFile("boardFile");
        if(file != null) {
            boardFile = multi.getOriginalFileName("boardFile");
            boardRealFile = file.getName();
        }


        BoardDTO parent = boardService.getBoard(boardID);
        boardService.replyUpdate(parent);
        boardService.reply(userID, boardTitle, boardContent, boardFile, boardRealFile,parent);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 답변이 작성되었습니다.");
        return "boardView";
    }

    @PostMapping("/BoardUpdate")
    public String BoardUpdate(HttpServletRequest request) {

        MultipartRequest multi = null;
        int fileMaxSize = 100 * 1024 * 1024;
        String savePath = request.getRealPath("/resources/upload").replaceAll("\\\\","/");
        try {
            multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
        }catch (Exception e) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","100mb 이상 불가" );
            return "index";
        }
        String userID = multi.getParameter("userID");
        HttpSession session = request.getSession();

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가4");
            return "index";
        }
        String boardID = multi.getParameter("boardID");
        if(boardID == null || boardID.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가5");
            return "index";
        }

        BoardDTO board = boardService.getBoard(boardID);
        if(!userID.contentEquals(board.getUserID())) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가6");
            return "index";
        }

        String boardTitle = multi.getParameter("boardTitle");
        String boardContent = multi.getParameter("boardContent");
        if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","내용을 모두 채워주세요");
            return "boardWrite";
        }

        String boardFile = "";
        String boardRealFile = "";
        File file = multi.getFile("boardFile");
        if(file != null) {
            boardFile = multi.getOriginalFileName("boardFile");
            boardRealFile = file.getName();
            String prev = boardService.getRealFile(boardID);
            File prevFile = new File(savePath + "/" + prev);
            if(prevFile.exists()) {
                prevFile.delete();
            }
        }else {
            boardFile = boardService.getFile(boardID);
            boardRealFile = boardService.getRealFile(boardID);
        }

        boardService.update(boardID, boardTitle, boardContent, boardFile, boardRealFile);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 게시물이 수정되었습니다.");
        return "./boardView";

    }

    @PostMapping("/BoardWrite")
    public String boardWrite(HttpServletRequest request) {

        MultipartRequest multi = null;
        int fileMaxSize = 100 * 1024 * 1024;
        String savePath = request.getRealPath("/resources/upload").replaceAll("\\\\","/");
        try {
            multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
        }catch (Exception e) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","100mb 이상 불가" );
            return "index";
        }
        String userID = multi.getParameter("userID");
        HttpSession session = request.getSession();

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
            return "index";
        }

        String boardTitle = multi.getParameter("boardTitle");
        String boardContent = multi.getParameter("boardContent");
        if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","내용을 모두 채워주세요");
            return "boardWrite";
        }

        String boardFile = "";
        String boardRealFile = "";
        File file = multi.getFile("boardFile");
        if(file != null) {
            boardFile = multi.getOriginalFileName("boardFile");
            boardRealFile = file.getName();
        }


        boardService.write(userID, boardTitle, boardContent, boardFile, boardRealFile);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 게시물이 작성되었습니다.");
        return "boardView";

    }

    @GetMapping("/boardWrite")
    public String boardWrite(Model model,HttpSession session) {

        UserDTO user = new UserDTO();
        if(session.getAttribute("userID")!=null) {
            user = userService.getUser((String) session.getAttribute("userID"));
            model.addAttribute("userID",user.getUserID());
        }

        return "boardWrite";
    }

    @GetMapping("/boardView")
    public String boardView(@RequestParam(value="boardTitle", defaultValue="") String boardTitle,
                            @RequestParam(value="pageNumber", defaultValue="1") String pageNumber,Model model,HttpServletRequest request) {


        model.addAttribute("boardService",boardService);
        model.addAttribute("commnetService",commentService);

        return "boardView";
    }

    @GetMapping("/boardShow")
    public String boardShow(@RequestParam(value="boardID",defaultValue="") String boardID ,Model model) {


        BoardDTO boardDTO = boardService.getBoard(boardID);
        UserService user = userService;
        boardService.hit(boardID);
        model.addAttribute("board",boardDTO);
        model.addAttribute("userService",user);

        return "boardShow";
    }


    @GetMapping("/boardUpdate")
    public String boardUpdate(@RequestParam("boardID") String boardID ,Model model) {


        model.addAttribute("userService",userService);
        model.addAttribute("boardService",boardService);


        return "boardUpdate";
    }

    @GetMapping("/boardDownload")
    public String boardDownload(@RequestParam("boardID") String boardID,Model model) {

        model.addAttribute("boardService",boardService);

        return "boardDownload";
    }



}
