package com.chong.girl.control;

import com.chong.girl.server.MyArticleService;
import org.pegdown.PegDownProcessor;
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

        model.addAttribute("article",myArticleService.findByID(articleID));
        return "article_content";
    }

    @GetMapping("/testcss")
    public String testCss() {
        return "testcss";
    }


}
