package com.chong.girl.control;

import com.chong.girl.bean.MyInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SelfController {

    @GetMapping("/selfIntroduce")
    public String selfIntroduce(Model model){
        MyInfo info = new MyInfo();
        info.setEmail("pompip@163.com");
        info.setName("刘科冲");
        info.setPhone("17091613625");
        info.setProfession("Android开发工程师");
        model.addAttribute("info",info);

        return "self_introduce.html";
    }
}
