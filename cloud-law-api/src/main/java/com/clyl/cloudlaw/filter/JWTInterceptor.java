package com.clyl.cloudlaw.filter;


import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.util.JwtUtil;
import com.clyl.cloudlaw.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.security.SignatureException;
import java.util.Map;

/**
 * @author Iced
 * @version 1.0
 * 2022/12/12
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        RestBean<Void> restBean;
        try {
            // 如果是option请求，直接放行
            if (method.equals("OPTION")) {
                return true;
            }
            String token = request.getHeader("Authorization");
            if (token == null || "".equals(token) || "null".equals(token)) {
                throw new SignatureException("token不合法");
            }
            Map<String, Object> res = JwtUtil.verify(token);
            String username = res.get("username").toString();
            String id = res.get("id").toString();
            if (username != null && !"".equals(username)) {
                if (JwtUtil.isTokenExpired(token)) {
                    String newToken = JwtUtil.createToken(username, id);
                    response.setHeader("Authorization", newToken);
                    redisUtil.set(username + "-" + id, token, 60 * 60 * 24 * 7);
                }
                return true;
            } else {
                restBean = new RestBean<>(401, "token已失效，请重新登录");
            }
        } catch (SignatureException e) {
            e.printStackTrace();
            restBean = new RestBean<>(401, "token不合法");
        } catch (Exception e) {
            e.printStackTrace();
            restBean = new RestBean<>(401, "token验签失败" + e.getMessage());
        }
        String jsonRes = new ObjectMapper().writeValueAsString(restBean);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(jsonRes);
        return false;
    }
}
