package com.example.nam21.DB.DAO;

import com.example.nam21.DB.DTO.UserDTO;

import java.util.HashMap;

public interface UserDAO {


    public UserDTO login(String userID, String userPassword);

    public UserDTO getUser(String userID);

    public UserDTO registerCheck(String userID);

    public int register(HashMap<String,Object> map  /*String userID, String userPassword, String userName, String userAge,
                        String userGender, String userEmail, String userProfile*/);
    public int update(HashMap<String,Object> map/*String userID,String userPassword, String userName, String userAge,
                      String userGender, String userEmail*/);

    public int profile(HashMap<String,Object> map/*String userID, String userProfile*/);

    public String getProfile(String userID);
}
