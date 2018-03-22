package com.chong.girl.control;

import com.chong.girl.bean.Girl;
import com.chong.girl.bean.MyInfo;
import com.chong.girl.dao.GirlRepository;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    //    @RequestMapping(value = "/hello" ,method = RequestMethod.GET)
//    public String say (){
//        return "hello spring boot";
//    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        LinkedList<Object> list = new LinkedList<>();
        Gson gson = new Gson();
        gson.fromJson("", new TypeToken<MyInfo>() {
        }.getType());
        TypeToken<List<Girl>> type = new TypeToken<List<Girl>>() {
        };
        TypeAdapter<List<Girl>> adapter = new Gson().getAdapter(type);
        return "index";

    }



    @GetMapping("/testcss")
    public String testCss() {
        return "testcss";
    }


}
