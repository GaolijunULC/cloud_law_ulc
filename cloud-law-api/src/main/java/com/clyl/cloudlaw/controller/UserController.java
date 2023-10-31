package com.clyl.cloudlaw.controller;


import com.clyl.cloudlaw.entity.User;
import com.clyl.cloudlaw.entity.detail.LoginDetail;
import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public RestBean<Object> login(@RequestBody LoginDetail loginDetail) throws IOException {
        return userService.login(loginDetail);
    }

    @PostMapping("/setAvatar")
    public RestBean<Object> setAvatar(@RequestBody User user) {
        return userService.setAvatar(user);
    }

    @PostMapping("/setName")
    public RestBean<Object> setName(@RequestBody User user){
        return userService.setName(user);
    }

    @PostMapping("/setPhone")
    public RestBean<Object> setPhone(@RequestBody User user){
        return userService.setPhone(user);
    }


//    @RequestMapping("/login")
//    public String login1(String code, String accessToken) throws Exception {
//
//        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid +
//                "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
//        String url1 = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + accessToken;
//
//        String result = "";
//        String result1 = "";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        CloseableHttpClient httpClient1 = HttpClients.createDefault();
//        CloseableHttpResponse response = null;
//        CloseableHttpResponse response1 = null;
//        try {
//            HttpGet get = new HttpGet(url);
//
//            HttpPost post = new HttpPost(url1);
//
//            response = httpClient.execute(get);
//
//            //设置请求体
//            JSONObject requestObj = new JSONObject();
//            requestObj.put("code", code);
//            post.setEntity(new StringEntity(requestObj.toJSONString(), StandardCharsets.UTF_8));
//            response1 = httpClient1.execute(post);
//
//            HttpEntity entity = response.getEntity();
//            HttpEntity entity1 = response1.getEntity();
//            if (entity != null) {
//                result = EntityUtils.toString(entity, "UTF-8");
//            }
//            if (entity1 != null) {
//                result1 = EntityUtils.toString(entity1, "UTF-8");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//            if (response1 != null) {
//                response1.close();
//            }
//        }
//
//        JSONObject jsonObject = JSON.parseObject(result);
//        String openid = jsonObject.getString("openid");
//        String sessionKey = jsonObject.getString("session_key");
//
//        // TODO：进行相关业务处理，如校验用户身份、生成 Token 等
//        return result + "\n" + result1;
//    }

}
