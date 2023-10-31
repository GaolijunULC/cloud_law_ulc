package com.clyl.cloudlaw.service.Impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.clyl.cloudlaw.entity.User;
import com.clyl.cloudlaw.entity.detail.LoginDetail;
import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.mapper.UserMapper;
import com.clyl.cloudlaw.service.UserService;
import com.clyl.cloudlaw.util.JwtUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author xiugou798
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Resource
    private UserMapper userMapper;


    @Override
    public RestBean<Object> login(LoginDetail loginDetail) throws IOException {
        String code = loginDetail.getCode();
        if (code == null || "".equals(code)) {
            return new RestBean<>(401, "登录失败", null);
        }
        log.info("登录------->" + code);
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
                "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String result = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new RestBean<>(402, "登录失败", e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
        }
        log.info("登录------->" + result);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        String sessionKey = jsonObject.getString("session_key");

        if ("".equals(openid) || openid == null) {
            return new RestBean<>(404, "登录失败", null);
        }
        User user = new User();
        user.setOpenId(openid);
        User check = userMapper.check(user);
        // 没有用户创建用户
        if (check == null) {
            String userId = UUID.randomUUID().toString().replaceAll("-", "");
            user.setUserId(userId);
            userMapper.addUser(user);
            String token = JwtUtil.createToken(openid, userId);
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("user", user);
            return new RestBean<>(200, "登录成功", res);
        }
        String token = JwtUtil.createToken(openid, user.getUserId());
        Map<String, Object> res = new HashMap<>();
        res.put("token", token);
        res.put("user", check);
        return new RestBean<>(200, "登录成功", res);
    }

    @Override
    public RestBean<Object> setAvatar(User user) {
        int i = userMapper.modAvatar(user);
        return i == 0
                ? new RestBean<>(401, "修改失败", null)
                : new RestBean<>(200, "修改成功", null);
    }

    @Override
    public RestBean<Object> setName(User user) {
        int i = userMapper.modName(user);
        return i == 0
                ? new RestBean<>(401, "修改失败", null)
                : new RestBean<>(200, "修改成功", null);
    }

    @Override
    public RestBean<Object> setPhone(User user) {
        int i = userMapper.modPhone(user);
        return i == 0
                ? new RestBean<>(401, "修改失败", null)
                : new RestBean<>(200, "修改成功", null);
    }
}
