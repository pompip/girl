package com.chong.girl.server;

import com.chong.girl.bean.MyArticle;
import com.chong.girl.dao.MyArticleRepository;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class MyArticleService {
    @Autowired
    MyArticleRepository myArticleRepository;
    private PegDownProcessor pdp = new PegDownProcessor();
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm yyyy-MM-dd");

    public void saveSeveralArticle(List<MultipartFile> files) throws Exception {
        ArrayList<MyArticle> list = new ArrayList<>();
        for (MultipartFile file : files) {
            String content = new String(file.getBytes());
            String substring = content.substring(0,content.length()>100?100:content.length());

            MyArticle myArticle = new MyArticle();
            myArticle.setArticleTitle(file.getOriginalFilename());
            myArticle.setArticleTime(format.format(new Date(System.currentTimeMillis())));
            myArticle.setArticleContent(content);
            myArticle.setArticleBrief(substring);
            list.add(myArticle);
        }
        myArticleRepository.save(list);
    }

    public List<MyArticle> findAll() {
        return myArticleRepository.findAll();
    }

    public MyArticle findByID(Long ID) {
        MyArticle one = myArticleRepository.findOne(ID);
        one.setArticleContent(pdp.markdownToHtml(one.getArticleContent()));
        return one;
    }
}
