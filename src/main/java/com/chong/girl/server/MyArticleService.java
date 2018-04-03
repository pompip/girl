package com.chong.girl.server;

import com.chong.girl.bean.MyArticle;
import com.chong.girl.dao.MyArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Service
public class MyArticleService {
    @Autowired
    MyArticleRepository myArticleRepository;
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    public void saveSeveralArticle(List<MultipartFile> files) throws Exception {
        ArrayList<MyArticle> list = new ArrayList<>();


        for (MultipartFile file : files) {
            String content = new String(file.getBytes());
            String substring = content.substring(0, content.length() > 100 ? 100 : content.length());

            MyArticle myArticle = new MyArticle();
            myArticle.setArticleTitle(file.getOriginalFilename());
            myArticle.setArticleTime(format.format(new Date()));
            myArticle.setArticleContent(content);
            myArticle.setArticleBrief(substring);
            list.add(myArticle);
        }
        myArticleRepository.save(list);
    }

    public List<MyArticle> findAll() {
        List<MyArticle> all = myArticleRepository.findAll(new Sort(Sort.Direction.DESC,"articleTime"));

        all.forEach(myArticle -> {
            String articleTime = myArticle.getArticleTime();
            Date parse = null;
            try {
                parse = dateFormat.parse(articleTime);
            } catch (ParseException e) {
                parse = new Date();
            }
            myArticle.setArticleTime(dateFormat.format(parse));
        });
        return all;

    }

    public MyArticle findByID(Long ID) {
        MyArticle myArticle = myArticleRepository.findOne(ID);
        String articleTime = myArticle.getArticleTime();
        Date parse = null;
        try {
            parse = dateFormat.parse(articleTime);
        } catch (ParseException e) {
            parse = new Date();
        }
        myArticle.setArticleTime(dateFormat.format(parse));
        return myArticle;
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
