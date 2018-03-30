package com.chong.girl.control;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class LoginController  {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpSession session){
        session.removeAttribute("username");
        return "success";
    }

    @PostMapping("/login")
    @ResponseBody
    public String loginResult(String name , String password, HttpSession session){


        if ("liukechong".equals(name)&&"314159".equals(password)){
            session.setAttribute("username",name);

            return "success";
        }else {
            return "false";
        }
    }

}
