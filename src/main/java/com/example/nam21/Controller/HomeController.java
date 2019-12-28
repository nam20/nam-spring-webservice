package com.example.nam21.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController {


    @GetMapping("/")
    public String Home(Model model, HttpSession session, HttpServletResponse response){
        String userID = null;
        if(session.getAttribute("userID") != null) userID = (String) session.getAttribute("userID");

      /*  String messageType = null;
        if(session.getAttribute("messageType") != null){
            messageType = (String) session.getAttribute("messageType");
        }
        String messageContent = null;*/
        if(session.getAttribute("messageContent") != null){
          /*  messageContent = (String) session.getAttribute("messageContent");*/

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }

       /* model.addAttribute("messageContent",messageContent);
        model.addAttribute("messageType",messageType);
        model.addAttribute("userID",userID);*/
        return "index";
    }



    @GetMapping("/login")
    public String login(Model model, HttpSession session, HttpServletResponse response)  {

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
         /*   messageContent = (String) session.getAttribute("messageContent");*/

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }
        if(userID != null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 되어있는 상태");

           return "index";
        }

       /* model.addAttribute("messageContent",messageContent);
        model.addAttribute("messageType",messageType);*/
        return "login";
    }


    @GetMapping("/join")
    public String join(Model model, HttpSession session, HttpServletResponse response){

        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }

        String messageType = null;
        if(session.getAttribute("messageType") != null){
            messageType = (String) session.getAttribute("messageType");
        }
        String messageContent = null;
        if(session.getAttribute("messageContent") != null){
            messageContent = (String) session.getAttribute("messageContent");

            session.removeAttribute("messageContent");
            session.removeAttribute("messageType");

        }

        if(userID != null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","현재 로그인 되어있는 상태");
            return "index";
        }


       /* model.addAttribute("messageContent",messageContent);
        model.addAttribute("messageType",messageType);*/
        return "join";
    }


    @GetMapping("/find")
    public String find(Model model, HttpSession session, HttpServletResponse response){

        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }

        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","로그인이 되어있지 않음.");

                return "index";
        }

        model.addAttribute("userID",userID);

        return "find";

    }


    @GetMapping("/box")
    public String box(Model model,HttpSession session, HttpServletResponse response){
        String userID = null;
        if(session.getAttribute("userID") != null) {
            userID = (String) session.getAttribute("userID");
        }

        if(userID == null){
            session.setAttribute("messageType", "오류 메시지");
            session.setAttribute("messageContent","로그인이 되어있지 않음.");
            return "index";
        }


       /* model.addAttribute("userID",userID);*/


        return "box";
    }


    @GetMapping("/logoutAction")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }






    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }


}
