package com.example.nam21.Service;

import com.example.nam21.DB.DAO.ChatDAO;
import com.example.nam21.DB.DTO.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {


    @Autowired
    private ChatDAO chatDAO;

    public ArrayList<ChatDTO> getChatListByID(String fromID, String toID, String chatID) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("fromID",fromID);
        map.put("toID",toID);
        map.put("chatID",chatID);
        ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();

        try {

            List<ChatDTO> chat = chatDAO.getChatListByID(map);

            Iterator<ChatDTO> iterator = chat.iterator();

            while(iterator.hasNext()) {
                ChatDTO chatDTO = iterator.next();
                chatDTO.setFromID(chatDTO.getFromID().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                chatDTO.setToID(chatDTO.getToID().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                chatDTO.setChatContent(chatDTO.getChatContent().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                int chatTime = Integer.parseInt(chatDTO.getChatTime().substring(11,13));
                String timeType = "오전";
                if(chatTime >= 12) {
                    timeType = "오후";
                    chatTime -= 12;
                }
                chatDTO.setChatTime(chatDTO.getChatTime().substring(0,11)+" "+timeType+" "+chatTime+":"+chatDTO.getChatTime().substring(14,16)+"");
                chatList.add(chatDTO);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }


        return chatList;
    }


    public ArrayList<ChatDTO> getChatListByRecent(String fromID, String toID, int number) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("fromID",fromID);
        map.put("toID",toID);
        map.put("number",number);
        ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();

        try {

            List<ChatDTO> chat = chatDAO.getChatListByRecent(map);

            Iterator<ChatDTO> iterator = chat.iterator();

            while(iterator.hasNext()) {
                ChatDTO chatDto = iterator.next();
                chatDto.setFromID(chatDto.getFromID().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                chatDto.setToID(chatDto.getToID().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                chatDto.setChatContent(chatDto.getChatContent().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                int chatTime = Integer.parseInt(chatDto.getChatTime().substring(11,13));
                String timeType = "오전";
                if(chatTime >= 12) {
                    timeType = "오후";
                    chatTime -= 12;
                }
                chatDto.setChatTime(chatDto.getChatTime().substring(0,11)+" "+timeType+" "+chatTime+":"+chatDto.getChatTime().substring(14,16)+"");
                chatList.add(chatDto);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return chatList;
    }


    public ArrayList<ChatDTO> getBox(String userID) {
        ArrayList<ChatDTO> chatList = new ArrayList<ChatDTO>();

        try {

            List<ChatDTO> chat = chatDAO.getBox(userID);

            Iterator<ChatDTO> iterator = chat.iterator();

            while (iterator.hasNext()) {
                ChatDTO chatDto = iterator.next();

                chatDto.setFromID(chatDto.getFromID().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                chatDto.setToID(chatDto.getToID().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                chatDto.setChatContent(chatDto.getChatContent().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>"));
                int chatTime = Integer.parseInt(chatDto.getChatTime().substring(11, 13));
                String timeType = "오전";
                if (chatTime >= 12) {
                    timeType = "오후";
                    chatTime -= 12;
                }
                chatDto.setChatTime(chatDto.getChatTime().substring(0, 11) + " " + timeType + " " + chatTime + ":" + chatDto.getChatTime().substring(14, 16) + "");
                chatList.add(chatDto);
            }

            for (int i = 0; i < chatList.size(); i++) {
                ChatDTO x = chatList.get(i);
                for (int k = 0; k < chatList.size(); k++) {
                    ChatDTO y = chatList.get(k);
                    if (x.getFromID().equals(y.getToID()) && x.getToID().equals(y.getFromID())) {
                        if (x.getChatID() < y.getChatID()) {
                            chatList.remove(x);
                            i--;
                            break;
                        } else {
                            chatList.remove(y);
                            k--;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return chatList;
    }


    public int submit(String fromID, String toID, String chatContent) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("fromID",fromID);
        map.put("toID",toID);
        map.put("chatContent",chatContent);

        try {
            return chatDAO.submit(map);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


    public int readChat(String fromID, String toID) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("fromID",fromID);
        map.put("toID",toID);

        try {
            return chatDAO.submit(map);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int getAllUnreadChat(String userID) {
        try {
            int result = chatDAO.getAllUnreadChat(userID);
            if(result != 0) {
                return result;
            }	return 0;
        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


    public int getUnreadChat(String fromID, String toID) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("fromID",fromID);
        map.put("toID",toID);

        try {
            int result = chatDAO.getUnreadChat(map);
            if(result > 0) {
                return result;
            }	return 0;
        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
