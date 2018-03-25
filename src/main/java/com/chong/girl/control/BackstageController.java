package com.chong.girl.control;

import com.chong.girl.server.MyArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

}
