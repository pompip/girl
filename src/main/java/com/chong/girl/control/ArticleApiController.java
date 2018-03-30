package com.chong.girl.control;

import com.chong.girl.bean.MyArticle;
import com.chong.girl.server.MyArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ArticleApiController {
    @Autowired
    MyArticleService service;

    @GetMapping("/findAllArticle")
    public List<MyArticle> findAllArticle() {
        return service.findAll();
    }

    @GetMapping("/findOneArticle")
    public MyArticle findAllArticle(@RequestParam Long id) {
        return service.findByID(id);
    }

    @PostMapping("/uploadArticle")
    @ResponseBody
    public Map<String, Object> saveMarkdownArticle(@RequestBody MyArticle myArticle) {
        Long newID = myArticle.getId();
        if (newID <= 0) {
            newID = service.addMyArticle(myArticle.getArticleContent(),myArticle.getArticleTitle()).getId();
        } else {
            service.updateMyArticle(newID, myArticle.getArticleContent(),myArticle.getArticleTitle());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", newID);
        map.put("state", "success");
        return map;

    }
}
