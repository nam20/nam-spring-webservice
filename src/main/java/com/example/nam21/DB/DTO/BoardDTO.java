package com.example.nam21.DB.DTO;

public class BoardDTO {

    private String userID;
    private int boardID;
    private String boardTitle;
    private String boardContent;
    private String boardDate;
    private int boardHit;
    private String boardFile;
    private String boardRealFile;
    private int boardGroup;
    private int boardSequence;
    private int boardLevel;
    private int boardAvailable;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getBoardID() {
        return boardID;
    }

    public void setBoardID(int boardID) {
        this.boardID = boardID;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getBoardDate() {
        return boardDate;
    }

    public void setBoardDate(String boardDate) {
        this.boardDate = boardDate;
    }

    public int getBoardHit() {
        return boardHit;
    }

    public void setBoardHit(int boardHit) {
        this.boardHit = boardHit;
    }

    public String getBoardFile() {
        return boardFile;
    }

    public void setBoardFile(String boardFile) {
        this.boardFile = boardFile;
    }

    public String getBoardRealFile() {
        return boardRealFile;
    }

    public void setBoardRealFile(String boardRealFile) {
        this.boardRealFile = boardRealFile;
    }

    public int getBoardGroup() {
        return boardGroup;
    }

    public void setBoardGroup(int boardGroup) {
        this.boardGroup = boardGroup;
    }

    public int getBoardSequence() {
        return boardSequence;
    }

    public void setBoardSequence(int boardSequence) {
        this.boardSequence = boardSequence;
    }

    public int getBoardLevel() {
        return boardLevel;
    }

    public void setBoardLevel(int boardLevel) {
        this.boardLevel = boardLevel;
    }

    public int getBoardAvailable() {
        return boardAvailable;
    }

    public void setBoardAvailable(int boardAvailable) {
        this.boardAvailable = boardAvailable;
    }
}
