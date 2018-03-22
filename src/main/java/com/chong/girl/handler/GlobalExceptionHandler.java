package com.chong.girl.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
//@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler( HttpServletRequest request,Exception e)  {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorCode", 500);
        mav.addObject("errorURL", request.getRequestURL());
        StringBuilder stackTrace = new StringBuilder(e.getMessage());
        stackTrace.append("\n\n");
        for (StackTraceElement element:e.getStackTrace()  ) {
            stackTrace.append(element).append("\n");
        }
        mav.addObject("errorStack", stackTrace.toString());
        mav.setViewName("error");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        logger.info("url={}", requestAttributes.getRequest().getRequestURI());
        return mav;
    }


    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView error404(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorCode", 404);
        mav.addObject("errorURL", request.getRequestURL());
        mav.addObject("errorStack", "请求地址错误");
        mav.setViewName("error");
        return mav;
    }


}
