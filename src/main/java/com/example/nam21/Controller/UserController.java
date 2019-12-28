package com.example.nam21.Controller;

import com.example.nam21.DB.DTO.UserDTO;
import com.example.nam21.Service.UserService;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.MultipartRequest;




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jdk.nashorn.internal.objects.NativeRegExp.exec;

@Controller
public class UserController {

    @Autowired
    UserService userService;


    @ResponseBody
    @PostMapping("/list")
    public String list(HttpServletRequest request, Model model){

        UserDTO result = new UserDTO();
        result = userService.getUser(request.getParameter("userID"));
        String userName = result.getUserName();
        return userName;
    }


    @GetMapping("/update")
    public String update(Model model, HttpSession session, HttpServletResponse response){

        String userID = null;
        UserDTO user = null;


        if(session.getAttribute("userID") != null) {
            //session.removeAttribute("user");
            userID = (String) session.getAttribute("userID");
            user = userService.getUser((String)session.getAttribute("userID"));
            session.setAttribute("user",user);
        }
       /* String messageType = null;
        if(session.getAttribute("messageType") != null){
            messageType = (String) session.getAttribute("messageType");
        }
        String messageContent = null;*/
        if(session.getAttribute("messageContent") != null){
         /*   messageContent = (String) session.getAttribute("messageContent");*/

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }
        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 안되어있는 상태");
            return "index";
        }

        /*model.addAttribute("messageContent",messageContent);
        model.addAttribute("messageType",messageType);
        model.addAttribute("userID",userID);
        model.addAttribute("user",user);*/
        return "update";
    }


    @GetMapping("/profileUpdate")
    public String profileUpdate(Model model, HttpSession session,HttpServletResponse response){

        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }
       /* String messageType = null;
        if(session.getAttribute("messageType") != null){
            messageType = (String) session.getAttribute("messageType");
        }
        String messageContent = null;*/
        if(session.getAttribute("messageContent") != null){
          /*  messageContent = (String) session.getAttribute("messageContent");*/

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }

        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 안되어있는 상태");
            return "index";

        }

       /* model.addAttribute("messageContent",messageContent);
        model.addAttribute("messageType",messageType);
        model.addAttribute("userID",userID);*/
        return "profileUpdate";
    }

    @ResponseBody
    @PostMapping(value="/UserFind", produces="application/text;charset=utf-8")
    public String userFind(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        /*
         * request.setCharacterEncoding("UTF-8");
         * response.setContentType("text/html;charset=UTF-8");
         */

        String userID = request.getParameter("userID");

        try {
            if (userID == null || userID.equals("")) {
                return "-1";
            } else if (userService.registerCheck(userID) == 0) {

                return find(userID);

            } else {
                return "-1";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "-1";
        }
    }

        public String find (String userID) throws Exception {
            StringBuffer result = new StringBuffer("");
            result.append("{\"userProfile\":\"" + userService.getProfile(userID) + "\"}");
            return result.toString();
        }



   @PostMapping("/userLogin")
    public String userLogidn(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /*
         * request.setCharacterEncoding("UTF-8");
         * response.setContentType("text/html;charset=UTF-8");
         */
       Pattern pattern = Pattern.compile("([a-zA-Z0-9]{1,20})");

        String userID = request.getParameter("userID");
        String userPassword = request.getParameter("userPassword");
        if(userID == null || userID.equals("") ||userPassword == null || userPassword.equals("")) {
             request.getSession().setAttribute("messageType", "오류 메시지");
             request.getSession().setAttribute("messageContent", "모든 내용을 입력");
             //response.sendRedirect("/login");
             return "login";


   }

          Matcher matcher = pattern.matcher(userID);
          Matcher matcher2 = pattern.matcher(userPassword);
            if(!matcher.matches() || !matcher2.matches()){
                request.getSession().setAttribute("messageType","오류 메시지");
                request.getSession().setAttribute("messageContent","영,숫자 최대 20자 입력");
                return "login";

            }

            int result = userService.login(userID, userPassword);
            if (result == 1) {
                request.getSession().setAttribute("userID", userID);
                request.getSession().setAttribute("messageType", "성공 메시지");
                request.getSession().setAttribute("messageContent", "로그인 성공");
                //response.sendRedirect("/");
                return "index";
            } else if (result == 2) {
                request.getSession().setAttribute("messageType", "오류 메시지");
                request.getSession().setAttribute("messageContent", "비밀번호 확인");
               // response.sendRedirect("/login");
                return "login";
            } else if (result == 0) {
                request.getSession().setAttribute("messageType", "오류 메시지");
                request.getSession().setAttribute("messageContent", "아이디 없음");
                //response.sendRedirect("/login");
                return "login";
            } else if (result == -1) {
                request.getSession().setAttribute("messageType", "오류 메시지");
                request.getSession().setAttribute("messageContent", "데이터베이스 오류");
                //response.sendRedirect("/login");
                return "login";
            }

        return "login";


    }

   @PostMapping("/userProfile")
    public String userProfile(HttpServletRequest request, HttpServletResponse response, @RequestParam("userProfile") MultipartFile mfile) throws IOException {

        //MultipartRequest multi = null;


        String savePath = "C:/projects/nam21/src/main/webapp/upload/";//request.getRealPath("/resources/static/upload").replaceAll("\\\\","/");
       String random = UUID.randomUUID().toString().replaceAll("-", "");
       File nfile = new File(savePath +  random.substring(20) +mfile.getOriginalFilename());
        try {
            if(!mfile.getOriginalFilename().equals(""))  mfile.transferTo(nfile);
            //multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
        }catch (Exception e) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","10mb 이상 불가"+savePath );

            return "profileUpdate";
        }
        String userID = request.getParameter("userID");
        HttpSession session = request.getSession();

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");

            return "index";
        }
        String fileName="";

        if(!mfile.getOriginalFilename().equals("")) {
               // (File) multi.getFile("userProfile");
            String ext = nfile.getName().substring(nfile.getName().lastIndexOf(".")+1);
            if(ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif")) {
                String prev = userService.getUser(userID).getUserProfile();
                File prevFile = new File (savePath  + prev);
                if(prevFile.exists()) {
                    prevFile.delete();
                }
                fileName = nfile.getName();
            }else {
                if(nfile.exists()) {
                    nfile.delete();
                }
                session.setAttribute("messageType", "오류 메시지");
                session.setAttribute("messageContent","이미지 파일만 업로드 가능");

                return "profileUpdate";

            }
        }

        userService.profile(userID, fileName);
        session.setAttribute("messageType", "성공 메시지");
        session.setAttribute("messageContent","성공적으로 프로필이 변경되었습니다.");
        return "index";

    }

    @ResponseBody
    @PostMapping("/userRegisterCheck")
    public String userRegisterCheck(HttpServletRequest request) {
        String userID = request.getParameter("userID");
        if(userID == null || userID.equals("")) return "-1";
        return userService.registerCheck(userID)+"";
    }



    @PostMapping("/userRegister")
    public String userRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userID = request.getParameter("userID");
        String userPassword1 = request.getParameter("userPassword1");
        String userPassword2 = request.getParameter("userPassword2");
        String userName = request.getParameter("userName");
        String userAge = request.getParameter("userAge");
        String userGender = request.getParameter("userGender");
        String userEmail = request.getParameter("userEmail");
        String userProfile = request.getParameter("userProfile");




        if(userID ==null || userID.equals("") || userPassword1 ==null || userPassword1.equals("") ||
                userPassword2 ==null || userPassword2.equals("") ||
                userName ==null || userName.equals("") ||
                userAge ==null || userAge.equals("") ||
                userGender ==null || userGender.equals("") ||
                userEmail ==null || userEmail.equals("")) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","모든 내용을 입력하세요." );
            //response.sendRedirect("/join");
            return "join";
        }


        Pattern pattern = Pattern.compile("([a-zA-Z0-9]{1,20})");

        Matcher matcher = pattern.matcher(userID);
        Matcher matcher2 = pattern.matcher(userPassword1);
        if(!matcher.matches() || !matcher2.matches()){
            request.getSession().setAttribute("messageType","오류 메시지");
            request.getSession().setAttribute("messageContent","영,숫자 최대 20자 입력");
            return "join";

        }


        if(!userPassword1.equals(userPassword2)) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","비밀번호가 서로 다릅니다." );
            //response.sendRedirect("/join");
            return "join";
        }

        int result= userService.register(userID, userPassword1, userName, userAge, userGender, userEmail, "");

        if(result==1) {

            request.getSession().setAttribute("userID", userID);
            request.getSession().setAttribute("messageType","성공 메시지" );
            request.getSession().setAttribute("messageContent","회원가입 성공." );
            //response.sendRedirect("/");
            return "index";
        }
        else {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","이미 존재하는 회원." );
            //response.sendRedirect("/join");
            return "join";

        }
    }


    @PostMapping("/userUpdate")
    public String userUpdate(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String userID = request.getParameter("userID");

        HttpSession session = request.getSession();


        String userPassword1 = request.getParameter("userPassword1");
        String userPassword2 = request.getParameter("userPassword2");
        String userName = request.getParameter("userName");
        String userAge = request.getParameter("userAge");
        String userGender = request.getParameter("userGender");
        String userEmail = request.getParameter("userEmail");


        if(userID ==null || userID.equals("") || userPassword1 ==null || userPassword1.equals("") ||
                userPassword2 ==null || userPassword2.equals("") ||
                userName ==null || userName.equals("") ||
                userAge ==null || userAge.equals("") ||
                userGender ==null || userGender.equals("") ||
                userEmail ==null || userEmail.equals("")) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","모든 내용을 입력하세요." );
            //response.sendRedirect("/update");
            return "update";
        }

        if(!userID.equals((String) session.getAttribute("userID"))) {
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","접근 불가");
            //response.sendRedirect("/");
            return "index";
        }

        if(!userPassword1.equals(userPassword2)) {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","비밀번호가 서로 다릅니다." );
            //response.sendRedirect("/update");
            return "update";
        }

        int result= userService.update(userID, userPassword1, userName, userAge, userGender, userEmail);

        if(result==1) {

            request.getSession().setAttribute("userID", userID);
            request.getSession().setAttribute("messageType","성공 메시지" );
            request.getSession().setAttribute("messageContent","회원정보 수정 성공." );
            //response.sendRedirect("/");
            return "index";
        }
        else {
            request.getSession().setAttribute("messageType","오류 메시지" );
            request.getSession().setAttribute("messageContent","데이터베이스 오류.");
            //response.sendRedirect("/update");
            return "update";

        }
    }




}
