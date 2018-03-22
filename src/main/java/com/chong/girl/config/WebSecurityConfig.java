package com.chong.girl.config;

import com.chong.girl.server.SpringWebSocketHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSecurityConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    //    @Bean
//    public SecurityInterceptor getSecurityInterceptor() {
//        return new SecurityInterceptor();
//    }
    @Autowired
    SecurityInterceptor securityInterceptor;
    @Autowired
    private SpringWebSocketHandler springWebSocketHandler;
    @Autowired
    private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
//        InterceptorRegistration interceptor = registry.addInterceptor(getSecurityInterceptor());
        InterceptorRegistration interceptor = registry.addInterceptor(securityInterceptor);
//        interceptor.excludePathPatterns("/account/**");
        interceptor.excludePathPatterns("/");
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(springWebSocketHandler, "/websocket")
                .addInterceptors(webSocketHandshakeInterceptor);

    }


    @Component
    static class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
        static int visitorCode = 1;

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            if (request instanceof ServletServerHttpRequest) {
                HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

                HttpSession session = servletRequest.getSession();
                LoggerFactory.getLogger(this.getClass()).info(session.getId());
                LoggerFactory.getLogger(this.getClass()).info(session.toString());

                Object username = session.getAttribute("username");
                if (username == null) {
                    username = "神秘人" + (visitorCode++);
                    session.setAttribute("username", username);

                }
                 attributes.put("username", username);
                return true;
            }
            return false;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

        }
    }

    @Component
    static class SecurityInterceptor extends HandlerInterceptorAdapter {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            HttpSession session = request.getSession();
            System.out.println("preHandle     " + session);
            if (session.getAttribute("user") != null) {

                return true;
            } else {
//                response.sendRedirect("/account/login");
                return true;
            }
        }
    }
}
