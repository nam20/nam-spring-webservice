package com.example.nam21.DB.DAO;

import com.example.nam21.DB.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDAOimpl implements UserDAO{

   @Autowired
   private UserDAO userDAO;



    @Override
    public UserDTO getUser(String userID) {

        UserDTO userDTO = userDAO.getUser(userID);
        if(userDTO!=null) return userDTO;
        return null;
    }

    @Override
    public int login(String userID, String userPassword) {
        return 0;
    }

    @Override
    public int registerCheck(String userID) {
        return 0;
    }

    @Override
    public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail, String userProfile) {
        return 0;
    }

    @Override
    public int update(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail) {
        return 0;
    }

    @Override
    public int profile(String userID, String userProfile) {
        return 0;
    }

    @Override
    public String getProfile(String userID) {
        return null;
    }
}
