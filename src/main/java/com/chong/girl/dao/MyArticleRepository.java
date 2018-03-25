package com.chong.girl.dao;

import com.chong.girl.bean.MyArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyArticleRepository extends JpaRepository<MyArticle,Long> {
}
