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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

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


        String prev = boardService.getRealFile(boardID);
        int result = boardService.delete(boardID);

        if(result == -1) {
            request.getSession().setAttribute("messageType", "오류 메시지");
            request.getSession().setAttribute("messageContent", "실패");
            return "index";
        }else {
            File prevFile = new File(  "C:/projects/nam21/src/main/webapp/upload/" + prev);
            if(prevFile.exists()) {
                prevFile.delete();
            }
            request.getSession().setAttribute("messageType", "성공 메시지");
            request.getSession().setAttribute("messageContent", "성공");
            return "boardView";
        }
    }

    @GetMapping("/boardReply")
    public String boardReply(HttpServletRequest request, Model model, HttpSession session, @RequestParam(value="boardID",defaultValue="") String boardID) {

        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }

        if(session.getAttribute("messageContent") != null){

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");
        }


        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 안되어있는 상태");

            return "index";
        }
        /*String boardID = null;
        if(request.getParameter("boardID") != null){
            boardID = (String) request.getParameter("boardID");
        }*/

        if(boardID.equals("")){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","게시물을 선택하세요");

            return "index";
        }

        UserDTO user = userService.getUser(userID);
        model.addAttribute("boardID",boardID);
        model.addAttribute("user",user);
        return "boardReply";
    }

    @PostMapping("/BoardReply")
    public String BoardReply(HttpServletRequest request,Model model, @RequestParam("boardFile") MultipartFile mfile) {



        //MultipartRequest multi = null;
        int fileMaxSize = 100 * 1024 * 1024;
        String savePath = "C:/projects/nam21/src/main/webapp/upload/";//request.getRealPath("/resources/upload").replaceAll("\\\\","/");
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        File nfile = new File("C:/projects/nam21/src/main/webapp/upload/"+ random.substring(20) + mfile.getOriginalFilename());
        try {
            if(!mfile.getOriginalFilename().equals(""))  mfile.transferTo(nfile);
            //multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
        }catch (Exception e) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","100mb 이상 불가" );
            return "index";
        }
        String userID = request.getParameter("userID");
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
        String boardID = request.getParameter("boardID");
        if(boardID == null || boardID.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
            return "index";
        }
        String boardTitle = request.getParameter("boardTitle");
        String boardContent = request.getParameter("boardContent");
        if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","내용을 모두 채워주세요");
            return "boardReply";
        }

        String boardFile = "";
        String boardRealFile = "";
        /*File file = multi.getFile("boardFile");
        if(file != null) {
            boardFile = multi.getOriginalFileName("boardFile");
            boardRealFile = file.getName();
        }*/
        if(!mfile.getOriginalFilename().equals("")) {
            boardFile = mfile.getOriginalFilename();//multi.getOriginalFileName("boardFile");
            boardRealFile = nfile.getName();
        }

        BoardDTO parent = boardService.getBoard(boardID);
        boardService.replyUpdate(parent);
        boardService.reply(userID, boardTitle, boardContent, boardFile, boardRealFile,parent);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 답변이 작성되었습니다.");
        return "boardView";
    }

    @PostMapping("/BoardUpdate")
    public String BoardUpdate(HttpServletRequest request,@RequestParam("boardFile") MultipartFile mfile) {

       // MultipartRequest multi = null;
        int fileMaxSize = 100 * 1024 * 1024;
        String savePath = "C:/projects/nam21/src/main/webapp/upload/";//request.getRealPath("/resources/upload").replaceAll("\\\\","/");
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        File nfile = new File("C:/projects/nam21/src/main/webapp/upload/"+ random.substring(20) + mfile.getOriginalFilename());
        try {
            if(!mfile.getOriginalFilename().equals(""))  mfile.transferTo(nfile);
            //multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
        }catch (Exception e) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","100mb 이상 불가" );
            return "index";
        }
        String userID = request.getParameter("userID");
        HttpSession session = request.getSession();

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가4");
            return "index";
        }
        String boardID = request.getParameter("boardID");
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

        String boardTitle = request.getParameter("boardTitle");
        String boardContent = request.getParameter("boardContent");
        if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","내용을 모두 채워주세요");
            return "boardWrite";
        }


       /* if(file != null) {
            boardFile = multi.getOriginalFileName("boardFile");
            boardRealFile = file.getName();
            String prev = boardService.getRealFile(boardID);
            File prevFile = new File(savePath + "/" + prev);
            if(prevFile.exists()) {
                prevFile.delete();
            }
        }*/

        String boardFile = "";
        String boardRealFile = "";
        //File file = multi.getFile("boardFile");
        if(!mfile.getOriginalFilename().equals("")) {
            boardFile = mfile.getOriginalFilename();//multi.getOriginalFileName("boardFile");
            boardRealFile = nfile.getName();
            String prev = boardService.getRealFile(boardID);
            File prevFile = new File(savePath  + prev);
            if(prevFile.exists()) {
                prevFile.delete();
            }
        }
       else {
            boardFile = boardService.getFile(boardID);
            boardRealFile = boardService.getRealFile(boardID);
        }

        boardService.update(boardID, boardTitle, boardContent, boardFile, boardRealFile);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 게시물이 수정되었습니다.");
        return "./boardView";

    }



    @PostMapping("/BoardWrite")
    public String boardWrite(HttpServletRequest request, @RequestParam("boardFile") MultipartFile mfile) {

        int fileMaxSize = 100 * 1024 * 1024;
        //String rootPath = request.getSession().getServletContext().getRealPath("/upload").replaceAll("\\\\","/") ;
        String savePath =  "C:/projects/nam21/src/main/webapp/upload";  //request.getRealPath("/upload").replaceAll("\\\\","/");
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        File nfile = new File("C:/projects/nam21/src/main/webapp/upload/"+ random.substring(20) + mfile.getOriginalFilename());

            try { if(!mfile.getOriginalFilename().equals(""))  mfile.transferTo(nfile);//multi = new MultipartRequest(request, "C:/projects/nam21/src/main/webapp/upload", 1024 * 1024 * 100, "UTF-8", new DefaultFileRenamePolicy());
            } catch (Exception e) {
                e.printStackTrace();
                request.getSession().setAttribute("messageType", "오류 메시지");
                request.getSession().setAttribute("messageContent", mfile.getOriginalFilename());
                return "index";
            }



        String userID = request.getParameter("userID");
        HttpSession session = request.getSession();

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
            return "index";
        }

        String boardTitle = request.getParameter("boardTitle");
        String boardContent = request.getParameter("boardContent");
        if(boardTitle == null || boardTitle.equals("") || boardContent == null || boardContent.equals("")) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","내용을 모두 채워주세요");
            return "boardWrite";
        }

        String boardFile = "";
        String boardRealFile = "";
        //multi.getFile("boardFile");
        if(!mfile.getOriginalFilename().equals("")) {

            boardFile = mfile.getOriginalFilename();//multi.getOriginalFileName("boardFile");
            boardRealFile = nfile.getName();
        }
        ArrayList<BoardDTO> boardList = boardService.getList("1");

        boardService.write(userID, boardTitle, boardContent, boardFile, boardRealFile);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 게시물이 작성되었습니다.");

        return "boardView";

    }



    @GetMapping("/boardWrite")
    public String boardWrite(Model model,HttpSession session) {


        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");

        }

        if(session.getAttribute("messageContent") != null){


            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }

        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 안되어있는 상태");
            return "login";
        }






        return "boardWrite";
    }

    @GetMapping("/boardView")
    public String boardView(@RequestParam(value="boardTitle", defaultValue="") String boardTitle,
                            @RequestParam(value="pageNumber", defaultValue="1") String pageNumber,Model model,HttpServletRequest request,HttpSession session) {

       /* String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }*/

        if(session.getAttribute("messageContent") != null) {

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }

       /* int targetPage = 0;
        ArrayList<BoardDTO> boardList = null;
        if(boardTitle.equals("")) {
            boardList = boardService.getList(pageNumber);
            targetPage = boardService.targetPage(pageNumber);
        }

        else {
            boardList = boardService.search(boardTitle);
            targetPage = boardService.searchTargetPage(pageNumber, boardTitle);
        }*/





        try{
            int intnum = Integer.parseInt(pageNumber);
            model.addAttribute("pageNumber",intnum);
            session.setAttribute("pageNumber",intnum);
            //session.setAttribute("pageNumber",intnum);
        }catch (Exception e){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","페이지 번호가 잘못되었다");
            return "boardView";
        }



        session.setAttribute("boardTitle",boardTitle);
        session.setAttribute("commentService",commentService);
        session.setAttribute("boardService",boardService);

       /* model.addAttribute("targetPage",targetPage);
        model.addAttribute("boardList",boardList);
        model.addAttribute("boardTitle",boardTitle);
        model.addAttribute("commentService",commentService);*/





        return "boardView";
    }

    @GetMapping("/boardShow")
    public String boardShow(@RequestParam(value="boardID",defaultValue="") String boardID ,Model model,HttpSession session) {


        if(session.getAttribute("messageContent") != null) {

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }

        if(boardID == null || boardID.equals("")){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","게시물을 선택하세요");
            return "index";
        }


        BoardDTO boardDTO = boardService.getBoard(boardID);
        UserService user = userService;

        if(boardDTO.getBoardAvailable() == 0){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","삭제된 게시물입니다.");

            return "boardView";
        }
        int writeTime = Integer.parseInt(boardDTO.getBoardDate().substring(11,13));
        String timeType = "오전";
        if(writeTime >= 12) {
            timeType = "오후";
            writeTime -= 12;
        }
        model.addAttribute("boardID",boardID);
        model.addAttribute("timeType",timeType);
        model.addAttribute("writeTime",writeTime);

        //session.setAttribute("board",boardDTO);
        boardService.hit(boardID);
        model.addAttribute("board",boardDTO);
        model.addAttribute("userService",user);

        return "boardShow";
    }


    @GetMapping("/boardUpdate")
    public String boardUpdate(@RequestParam("boardID") String boardID ,Model model,HttpSession session,HttpServletRequest request) {


        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }

        if(session.getAttribute("messageContent") != null){


            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }


        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 안되어있는 상태");

            return "login";
        }

        UserDTO user = userService.getUser(userID);//new UserDAOImpl().getUser(userID);


        if(boardID == null || boardID.equals("")){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가1");

            return "index";
        }

        BoardDTO board = boardService.getBoard(boardID);
        if(!userID.equals(board.getUserID())){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가2");

            return "index";
        }

        model.addAttribute("user",user);
        model.addAttribute("board",board);


        return "boardUpdate";
    }


    @RequestMapping("/boardDownload")
    public void boardDownload(@RequestParam("boardID") String boardID, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {

        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(boardID == null || boardID.equals("")){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");

            response.sendRedirect("/");
        }

        String root = request.getSession().getServletContext().getRealPath("/");
        String savePath = "C:/projects/nam21/src/main/webapp/upload/";
        String fileName = "";
        String realFile = "";
        fileName = boardService.getFile(boardID);
        realFile = boardService.getRealFile(boardID);
        if(fileName.equals("") || realFile.equals("")){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
           response.sendRedirect("/");
        }

        InputStream in = null;
        OutputStream os = null;
        File file = null;
        boolean skip = false;
        String client = "";



        try{
            try{
                file = new File(savePath,realFile);
                in = new FileInputStream(file);
            }catch(FileNotFoundException e){
                skip = true;
            }
            client = request.getHeader("User-Agent");
            response.reset();
            response.setContentType("application/octet-stream");


            if(!skip){
                if(client.indexOf("MSIE") != -1){
                    response.setHeader("Content-Disposition","attachment; filename=" + new String(fileName.getBytes("KSC5601"),"ISO8859_1"));
                }else{
                    fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");

                    response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
                    response.setHeader("Content-Type","application/octet-stream; charset=UTF-8");
                }
                response.setContentLength((int)file.length());
                os = response.getOutputStream();
                try{
                    FileCopyUtils.copy(in,os);
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(in != null){
                        try {
                            in.close();
                            os.flush();
                        }catch (Exception e) {}
                    }
                }


            } else{
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.println("<script>alert('파일을 찾을 수 없다.');history.back();</script>");
                writer.flush();



            }
        }catch(Exception e){
            e.printStackTrace();
        }










    }



}
