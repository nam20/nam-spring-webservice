package com.example.nam21.Controller;

import com.example.nam21.DB.DTO.ChatDTO;
import com.example.nam21.Service.ChatService;
import com.example.nam21.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

@Controller
public class ChatController {

    @Autowired
    ChatService chatService;

    @Autowired
    UserService userService;

    @ResponseBody
    @PostMapping(value="/chatBox", produces="application/text;charset=utf-8")
    public String chatBox(HttpServletRequest request) {

        String userID = request.getParameter("userID");
        if(userID == null || userID.equals("")) {
            return "";
        }else {
            try {

                HttpSession session = request.getSession();
                if(!URLDecoder.decode(userID,"UTF-8").equals((String) session.getAttribute("userID"))) {
                    return "";
                }
                userID = URLDecoder.decode(userID,"UTF-8");
                return getBox(userID);
            }catch (Exception e) {
                return "";
            }


        }

    }

    public String getBox(String userID) {

        StringBuffer result = new StringBuffer("");
        result.append("{\"result\":[");

        ArrayList<ChatDTO> chatList = chatService.getBox(userID);
        if(chatList.size()==0) return "";
        for(int i = chatList.size()-1; i >= 0; i--) {

            String unread = "";
            String userProfile = "";
            if(userID.equals(chatList.get(i).getToID())) {
                unread = chatService.getUnreadChat(chatList.get(i).getFromID(), userID)+"";
                if(unread.equals("0")) unread = "";
            }

            if(userID.equals(chatList.get(i).getToID())) {
                userProfile = userService.getProfile(chatList.get(i).getFromID());
            }else {
                userProfile = userService.getProfile(chatList.get(i).getToID());

            }
            result.append("[{\"value\": \"" + chatList.get(i).getFromID() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getToID() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getChatContent() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getChatTime() +"\"},");
            result.append("{\"value\": \"" + unread +"\"},");
            result.append("{\"value\": \"" + userProfile +"\"}]");
            if(i != 0) result.append(",");
        }

        result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID()+"\"}");

        return result.toString();
    }


    @PostMapping(value="/chatList", produces="application/text;charset=utf-8")
    public @ResponseBody String chatList(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String fromID = request.getParameter("fromID");
        String toID = request.getParameter("toID");
        String listType = request.getParameter("listType");


        if(fromID == null || fromID.equals("") || toID == null || toID.equals("")
                || listType == null || listType.equals("")){
            return "";
        }else if (listType.equals("ten")) return getTen(URLDecoder.decode(fromID,"UTF-8"),URLDecoder.decode(toID,"UTF-8"));
        else {
            try {
                HttpSession session = request.getSession();
                if(!URLDecoder.decode(fromID,"UTF-8").equals((String) session.getAttribute("userID"))) {
                    return "";
                }

                return getID(fromID,toID,listType);

            }catch (Exception e) {
                e.printStackTrace();
                return "";

            }
        }

    }

    public String getTen(String fromID, String toID) {
        StringBuffer result = new StringBuffer("");
        result.append("{\"result\":[");

        ArrayList<ChatDTO> chatList = chatService.getChatListByRecent(fromID, toID, 100);
        if(chatList.size()==0) return "";
        for(int i=0; i<chatList.size(); i++) {

            result.append("[{\"value\": \"" + chatList.get(i).getFromID() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getToID() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getChatContent() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getChatTime() +"\"}]");
            if(i != chatList.size() -1) result.append(",");
        }

        result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID()+"\"}");
        chatService.readChat(fromID, toID);
        return result.toString();
    }

    public String getID(String fromID, String toID, String chatID) {
        StringBuffer result = new StringBuffer("");
        result.append("{\"result\":[");

        ArrayList<ChatDTO> chatList = chatService.getChatListByID(fromID, toID, chatID);
        if(chatList.size()==0) return "";
        for(int i=0; i<chatList.size(); i++) {

            result.append("[{\"value\": \"" + chatList.get(i).getFromID() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getToID() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getChatContent() +"\"},");
            result.append("{\"value\": \"" + chatList.get(i).getChatTime() +"\"}]");
            if(i != chatList.size() -1) result.append(",");
        }

        result.append("], \"last\":\"" + chatList.get(chatList.size()-1).getChatID()+"\"}");
        chatService.readChat(fromID, toID);
        return result.toString();
    }


    @ResponseBody
    @RequestMapping(method=RequestMethod.POST,value="/chatSubmit")
    public String chatSubmit(HttpServletRequest request) throws UnsupportedEncodingException {

        String fromID = request.getParameter("fromID");
        String toID = request.getParameter("toID");
        String chatContent = request.getParameter("chatContent");

        if(fromID == null || fromID.equals("") || toID == null || toID.equals("")
                || chatContent == null || chatContent.equals("")){
            return "0";
        }else if(fromID.equals(toID)) {
            return "-1";
        }

        else {
            fromID= URLDecoder.decode(fromID, "UTF-8");
            toID= URLDecoder.decode(toID, "UTF-8");
            HttpSession session = request.getSession();
            if(!URLDecoder.decode(fromID,"UTF-8").equals((String) session.getAttribute("userID"))) {
                return "";
            }
            chatContent = URLDecoder.decode(chatContent,"UTF-8");
            return chatService.submit(fromID, toID, chatContent)+"";

        }

    }






        @GetMapping("/chat")
        public String chat(Model model, HttpSession session, HttpServletResponse response, @RequestParam("toID") String toID) throws IOException {

            String userID = null;
            if(session.getAttribute("userID") != null) {
                userID = (String) session.getAttribute("userID");
            }
     /*   String toID= null;
        if (request.getAttribute("toID") != null){
            toID = (String) request.getAttribute("toID");
        }
       */

            if(userID == null){
                session.setAttribute("messageType", "오류 메시지");
                session.setAttribute("messageContent","현재 로그인 안되어있는 상태");
                response.sendRedirect("/");

            }

            if(toID == null){
                session.setAttribute("messageType", "오류 메시지");
                session.setAttribute("messageContent","대화 상대가 지정되지않음");
                response.sendRedirect("/");

            }

            if(userID.equals(URLDecoder.decode(toID,"UTF-8"))){
                session.setAttribute("messageType", "오류 메시지");
                session.setAttribute("messageContent","자신에게 보낼 수 없음");
                response.sendRedirect("/");

            }


            String fromProfile = userService.getProfile((String) session.getAttribute("userID"));
            String toProfile = userService.getProfile(toID);

           /* modelAndView.addObject("fromProfile",fromProfile);
            modelAndView.addObject("toProfile",toProfile);*/

            model.addAttribute("fromProfile",fromProfile);
            model.addAttribute("toProfile",toProfile);


            return "chat";
        }







    @ResponseBody
    @PostMapping(value="/chatUnread")
    public String chatUnread(HttpServletRequest request) throws UnsupportedEncodingException {

        String userID = request.getParameter("userID");
        if(userID == null || userID.equals("")) {
            return "0";
        }else {
            userID = URLDecoder.decode(userID, "UTF-8");
            HttpSession session = request.getSession();
            if(!URLDecoder.decode(userID,"UTF-8").equals((String) session.getAttribute("userID"))) {
                return "";
            }
            return chatService.getAllUnreadChat(userID)+"";

        }
    }


}
