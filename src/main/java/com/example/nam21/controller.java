package com.example.nam21;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class controller {

    @GetMapping("/hello")
    public String getmap(){
        return "hello";
    }
    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("name","namheejun");
        return "index";
    }


}
