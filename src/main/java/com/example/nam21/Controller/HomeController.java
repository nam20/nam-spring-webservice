package com.example.nam21.Controller;

import com.example.nam21.DB.DAO.UserDAO;
import com.example.nam21.Huge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    UserDAO userDAO;

    @GetMapping("/")
    public String Home(Model model){
        Huge huge = new Huge();
        huge.setValue("skagmlwns123");
        huge.setId(4232);

        model.addAttribute("huge",huge);
        return "index";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
