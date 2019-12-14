package com.example.nam21.Service;

import com.example.nam21.DB.DAO.BoardDAO;
import com.example.nam21.DB.DTO.BoardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BoardService {




    @Autowired
    private BoardDAO boardDAO;


    public int write(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {

        try {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("userID",userID);
            map.put("boardTitle",boardTitle);
            map.put("boardContent",boardContent);
            map.put("boardFile",boardFile);
            map.put("boardRealFile",boardRealFile);

            return boardDAO.write(map);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public BoardDTO getBoard(String boardID) {
        return boardDAO.getBoard(Integer.parseInt(boardID));
    }


    public ArrayList<BoardDTO> getList(String pageNumber) {

        int page1= Integer.parseInt(pageNumber) * 10;
        int page2= (Integer.parseInt(pageNumber) - 1) * 10;
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("page1",page1);
        map.put("page2",page2);
        ArrayList<BoardDTO> boardList = new ArrayList<BoardDTO>();

        try {
            List<BoardDTO> board = boardDAO.getList(map);

            Iterator<BoardDTO> iterator = board.iterator();

            while(iterator.hasNext()) {
                BoardDTO boardDto = iterator.next();
                boardDto.setBoardTitle(boardDto.getBoardTitle().replace(" ","&nbsp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>"));
                boardDto.setBoardContent(boardDto.getBoardContent().replace(" ","&nbsp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>"));
                boardDto.setBoardDate(boardDto.getBoardDate().replace(" ","&nbsp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>"));
                boardList.add(boardDto);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }


        return boardList;

    }


    public int hit(String boardID) {
        try {
            return boardDAO.hit(boardID);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


    public String getFile(String boardID) {
        try {
            String file = boardDAO.getFile(boardID);

            if(file != null) {
                return file;
            }	return "";
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    public String getRealFile(String boardID) {
        try {
            String file = boardDAO.getRealFile(boardID);
            return file;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public int update(String boardID, String boardTitle, String boardContent, String boardFile, String boardRealFile) {
        try {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("boardID",Integer.parseInt(boardID));
            map.put("boardTitle",boardTitle);
            map.put("boardContent",boardContent);
            map.put("boardFile",boardFile);
            map.put("boardRealFile",boardRealFile);

            return boardDAO.update(map);

        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


    public int delete(String boardID) {
        try {
            return boardDAO.delete(boardID);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int reply(String userID, String boardTitle, String boardContent, String boardFile, String boardRealFile, BoardDTO parent) {
        try {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("userID",userID);
            map.put("boardTitle",boardTitle);
            map.put("boardContent",boardContent);
            map.put("boardFile",boardFile);
            map.put("boardRealFile",boardRealFile);
            map.put("parentBoardGroup",parent.getBoardGroup());
            map.put("parentBoardSequence",parent.getBoardSequence() + 1);
            map.put("parentBoardLevel",parent.getBoardLevel() + 1);
            return boardDAO.reply(map);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return -1;
    }


    public int replyUpdate(BoardDTO parent) {

        try {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("parentBoardGroup",parent.getBoardGroup());
            map.put("parentBoardSequence",parent.getBoardSequence());

            return boardDAO.replyUpdate(map);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public boolean nextPage(String pageNumber) {
        int pageNum = Integer.parseInt(pageNumber) * 10;

        try {

            BoardDTO board = boardDAO.nextPage(pageNumber);
            if(board != null) {
                return true;
            }else {
                board = new BoardDTO();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public int targetPage(String pageNumber) {
        int pageNum = (Integer.parseInt(pageNumber) - 1) * 10;


        try {
            int count = boardDAO.targetPage(pageNumber);
            if(count > 0) {

                return count/10;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int searchTargetPage(String pageNumber, String boardTitle) {
        int pageNum = (Integer.parseInt(pageNumber) - 1) * 10;
        boardTitle = "%" + boardTitle + "%";

        try {
            int count = boardDAO.searchTargetPage(boardTitle);
            if(count % 10 == 0) {
                count -= 1;
                //return (count - pageNum)/10;
            }

            return (count - pageNum)/10;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public ArrayList<BoardDTO> search(String boardTitle) {
        ArrayList<BoardDTO> boardList = new ArrayList<BoardDTO>();
        String title = "%" + boardTitle + "%";

        try {
            List<BoardDTO> board = boardDAO.search(boardTitle);

            Iterator<BoardDTO> iterator = board.iterator();
            while(iterator.hasNext()) {
                BoardDTO boardDto = iterator.next();
                boardDto.setBoardTitle(boardDto.getBoardTitle().replace(" ","&nbsp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>"));
                boardDto.setBoardContent(boardDto.getBoardContent().replace(" ","&nbsp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>"));
                boardDto.setBoardDate(boardDto.getBoardDate().replace(" ","&nbsp;").replace("<","&lt;").replace(">","&gt;").replace("\n","<br>"));
                boardList.add(boardDto);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        return boardList;
    }
}
