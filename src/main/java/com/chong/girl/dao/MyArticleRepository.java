package com.chong.girl.dao;

import com.chong.girl.bean.MyArticle;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MyArticleRepository extends JpaRepository<MyArticle,Long> {

    @Modifying
    @Query("update MyArticle article set article .articleContent= ?1 ,article .articleTitle =?2 where article.id= ?3")
    int updateArticleContent(String content,String title,Long id);
}
