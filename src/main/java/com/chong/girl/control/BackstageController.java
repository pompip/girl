package com.chong.girl.control;

import com.chong.girl.server.MyArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class BackstageController {

    @Autowired
    MyArticleService articleService;

    @GetMapping("/backstage")
    public String backstageIndex() {
        return "backstage";

    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(List<MultipartFile> files) throws Exception {
        articleService.saveSeveralArticle(files);
        return "scucess";
    }

    @PostMapping("/uploadarticle")
    @ResponseBody
    public Long saveMarkdownArticle(@RequestParam String content, @RequestParam String title, @RequestParam Long id) {
        if (id <=0){
          return   articleService.addMyArticle(content, title).getId();
        }else{
            articleService.updateMyArticle(id, content, title);
            return id;
        }
    }
    @PostMapping("/deletearticle")
    @ResponseBody
    public boolean deleteArticle(@RequestParam Long id){
        return articleService.deleteMyArticleByID(id);
    }
    @GetMapping("/markdown")
    public String markdown() {
        return "markdown";
    }
    @GetMapping("/markdown/{id}")
    public String markdown(@PathVariable("id") Long articleID,Model model) {
        model.addAttribute("article",articleService.findMarkDownContentByID(articleID));
        return "markdown";
    }
}
