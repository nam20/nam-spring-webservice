package com.example.nam21.DB.DAO;

import com.example.nam21.DB.DTO.UserDTO;

public interface UserDAO {


    public int login(String userID, String userPassword);

    public UserDTO getUser(String userID);

    public int registerCheck(String userID);

    public int register(String userID,String userPassword, String userName, String userAge,
                        String userGender, String userEmail, String userProfile);
    public int update(String userID,String userPassword, String userName, String userAge,
                      String userGender, String userEmail);

    public int profile(String userID, String userProfile);

    public String getProfile(String userID);
}
