package com.chong.girl.control;

import com.chong.girl.bean.MyInfo;
import com.chong.girl.server.MyArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloController {

    @Autowired
    MyArticleService myArticleService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        model.addAttribute("articleList", myArticleService.findAll());
        return "article_list";
    }

    @GetMapping("/article/{id}")
    public String articleContent(@PathVariable("id") Long articleID, Model model) {
        model.addAttribute("article", myArticleService.findByID(articleID));
        return "article_content";
    }

    @GetMapping("/testcss")
    public String testCss() {
        return "testcss";
    }
    @GetMapping("/vueTest")
    public String testVue() {
        return "vueTest";
    }


    @GetMapping("/archives")
    public String archives(Model model) {
        model.addAttribute("articleList", myArticleService.findAll());
        return "archives";
    }

    @GetMapping("/resume")
    public String selfIntroduce(Model model) {
        MyInfo info = new MyInfo();
        info.setEmail("pompip@163.com");
        info.setName("刘科冲");
        info.setPhone("17091613625");
        info.setProfession("Android开发工程师");
        model.addAttribute("info", info);

        return "resume";
    }

    @GetMapping("/chat")
    public String startChat() {
        return "chat";
    }
}
