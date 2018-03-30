package com.chong.girl.server;

import com.chong.girl.bean.MyArticle;
import com.chong.girl.dao.MyArticleRepository;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            String substring = content.substring(0, content.length() > 100 ? 100 : content.length());

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

    public MyArticle findMarkDownContentByID(Long ID) {
        return myArticleRepository.findOne(ID);
    }

    public MyArticle addMyArticle(String articleContent, String title) {
        MyArticle myArticle = new MyArticle();
        myArticle.setArticleContent(articleContent);
        myArticle.setArticleTitle(title);
        myArticle.setArticleTime(format.format(new Date(System.currentTimeMillis())));
        String substring = articleContent.substring(0, articleContent.length() > 100 ? 100 : articleContent.length());
        myArticle.setArticleBrief(substring);
        return myArticleRepository.save(myArticle);
    }

    @Transactional
    public boolean updateMyArticle(Long id, String articleContent, String title) {
        int i = myArticleRepository.updateArticleContent(articleContent, title, id);
        return i > 0;
    }

    @Transactional
    public boolean deleteMyArticleByID(Long id) {
        try {
            myArticleRepository.delete(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
