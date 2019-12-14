package com.example.nam21.DB.DAO;

import com.example.nam21.DB.DTO.ChatDTO;

import java.util.ArrayList;
import java.util.HashMap;

public interface ChatDAO {

    public ArrayList<ChatDTO> getChatListByID( HashMap<String,Object> map/*String fromID,  String toID,  String chatID*/);

    public ArrayList<ChatDTO> getChatListByRecent(HashMap<String,Object> map/*String fromID, String toID, int number*/);

    public ArrayList<ChatDTO> getBox(String userID);

    public int submit( HashMap<String,Object> map/*String fromID, String toID, String chatContent*/);

    public int readChat(HashMap<String,Object> map/*String fromID, String toID*/);

    public int getAllUnreadChat(String userID);

    public int getUnreadChat(HashMap<String,Object> map/*String fromID, String toID*/);

}
