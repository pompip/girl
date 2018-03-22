package com.chong.girl.dao;

import com.chong.girl.bean.Girl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GirlRepository extends JpaRepository<Girl,Integer> {

     List<Girl> findGirlByAge(Integer age);
}
