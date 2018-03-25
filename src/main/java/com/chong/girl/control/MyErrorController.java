package com.chong.girl.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.certpath.OCSPResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//@Controller
public class MyErrorController
        implements ErrorController {
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    //    @Autowired
//    private ErrorAttributes errorAttributes;
    private final static String ERROR_PATH = "/error";


    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("errorCode", 100);
        mav.addObject("errorURL", request.getRequestURL());
        mav.setViewName("error");
        return mav;
//        }

    }



    private boolean getTraceParameter(HttpServletRequest request) {
        String parameter = request.getParameter("trace");
        if (parameter == null) {
            return false;
        }
        return !"false".equals(parameter.toLowerCase());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception ex) {
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
//
//    private Map<String, Object> getAttributes(HttpServletRequest request,
//                                              boolean includeStackTrace) {
//        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
//        Map<String, Object> map = errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
//        String URL = request.getRequestURL().toString();
//        map.put("URL", URL);
//        return map;
//    }
}
