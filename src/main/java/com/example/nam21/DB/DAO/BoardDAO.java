package com.example.nam21.DB.DAO;

import com.example.nam21.DB.DTO.BoardDTO;

import java.util.ArrayList;
import java.util.HashMap;

public interface BoardDAO {

    public int write(HashMap<String,Object> map/*String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile*/);

    public BoardDTO getBoard(int boardID);

    public ArrayList<BoardDTO> getList(HashMap<String,Object> map/*String pageNumber*/);

    public int hit(String boardID);

    public String getFile(String boardID);

    public String getRealFile(String boardID);

    public int update(HashMap<String,Object> map/*String boardID, String boardTitle, String boardContent, String boardFile, String boardRealFile*/);

    public int delete(String boardID);

    public int reply(HashMap<String,Object> map/*String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile, BoardDTO parent*/);

    public int replyUpdate(HashMap<String,Object> map/*BoardDTO parent*/);

    public BoardDTO/*boolean*/ nextPage(String pageNumber);

    public int targetPage(String pageNumber);

    public int searchTargetPage(/*String pageNumber,*/ String boardTitle);

    public ArrayList<BoardDTO> search(String boardTitle);

}
