package com.example.nam21.Service;

import com.example.nam21.DB.DAO.UserDAO;
import com.example.nam21.DB.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    public int login(String userID, String userPassword) {
            try{
                UserDTO userDTO = userDAO.getUser(userID);
                if(userDTO!=null){
                    if(userPassword.equals(userDTO.getUserPassword())){
                        return 1;
                    }
                        return 2; //비민번호 오류
                }else {
                        return 0; //아이디 오류
                }
            }catch(Exception e){
                e.printStackTrace();
            }
                        return -1;
    }


    public UserDTO getUser(String userID) {
        UserDTO userDTO = userDAO.getUser(userID);
        if(userDTO!=null) return userDTO;
        return null;
    }


    public int registerCheck(String userID) {
        try{
            UserDTO userDTO = userDAO.getUser(userID);
            if(userDTO!=null || userID.equals("")){
                return 0;  //아이디 있음
            }else{
                return 1;  //성공
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    public int register(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail, String userProfile) {

        try{
            HashMap<String,Object> map = new HashMap<>();
            map.put("userID",userID);
            map.put("userPassword",userPassword);
            map.put("userName",userName);
            map.put("userAge",Integer.parseInt(userAge));
            map.put("userGender",userGender);
            map.put("userEmail",userEmail);
            map.put("userProfile",userProfile);

            return userDAO.register(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    public int update(String userID, String userPassword, String userName, String userAge, String userGender, String userEmail) {
        try{
            HashMap<String,Object> map = new HashMap<>();
            map.put("userID",userID);
            map.put("userPassword",userPassword);
            map.put("userName",userName);
            map.put("userAge",Integer.parseInt(userAge));
            map.put("userGender",userGender);
            map.put("userEmail",userEmail);


            return userDAO.update(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    public int profile(String userID, String userProfile) {
        try {
            HashMap <String,Object> map = new HashMap<String,Object>();
            map.put("userProfile",userProfile);
            map.put("userID",userID);

            return userDAO.profile(map);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public String getProfile(String userID) {
        try {

            String rs = userDAO.getProfile(userID);
            if(rs!=null) {
                if(rs.equals("")) {
                    return "./img/icon.png";
                }
                return "./upload/" + rs;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return "./img/icon.png";
    }
}
