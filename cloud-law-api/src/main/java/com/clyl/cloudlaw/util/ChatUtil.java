package com.clyl.cloudlaw.util;

import com.alibaba.fastjson.JSON;
import com.clyl.cloudlaw.entity.Msg;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.ConvertingEncoderDecoderSupport;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ChatUtil {

    @Value("${chat.key}")
    private String key;

    public String sendMsg(String msg) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        String content = "{\r\n\"model\":\"gpt-3.5-turbo\",\r\n\"messages\": [{\"role\": \"user\", \"content\": \"" + msg + "\"}],\r\n\"temperature\": 0.2\r\n}";
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url("https://api.aiproxy.io/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();


//        Reader reader = response.body().charStream();
//        while (true) {
//            int read = reader.read();
//            if (read == -1) {
//                break;
//            }
//            char c = (char) read;
//            System.out.println(c);
//        }


        String string = response.body().string();
        JSONObject jsonObject = new JSONObject(string);
        System.out.println(jsonObject);
        String choices = ((JSONObject) jsonObject.getJSONArray("choices").get(0)).getJSONObject("message").getString("content");
        log.info("回复:" + choices);


//        JSONParser parser = new JSONParser();
//        String parse = "";
//        try {
//            parse = (String) ((JSONObject) ((JSONObject) ((JSONArray) ((JSONObject) parser.parse(string)).get("choices")).get(0)).get("message")).get("content");
//            System.out.println(parse);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        return choices;
    }


    public String sendAllMsg(String preMsg, String msg) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build();
        JSONObject sendMsgJSON = new JSONObject();
        sendMsgJSON.put("role","user");
        sendMsgJSON.put("content",msg);
        String sendMsg = sendMsgJSON.toString();
//        String sendMsg = "{\"role\": \"user\", \"content\": \"" + msg + "\"}";
        MediaType mediaType = MediaType.parse("application/json");
        String content = "{\r\n\"model\":\"gpt-3.5-turbo\",\r\n\"messages\": [" + preMsg + "," + sendMsg + "],\r\n\"temperature\": 0.2\r\n}";
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url("https://api.aiproxy.io/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        JSONObject jsonObject = new JSONObject(string);
        System.out.println(jsonObject);
        JSONObject choices = ((JSONObject) jsonObject.getJSONArray("choices").get(0)).getJSONObject("message");
        log.info("回复:" + choices.getString("content"));
        log.info("所有信息：" + preMsg + "," + sendMsg + "," + choices);
        return choices.getString("content");
    }


    public String formatMessage(List<Msg> msgList) {
        String res = "";
        for (int i = 0; i < msgList.size(); i++) {
            Msg msg = msgList.get(i);
            JSONObject jsonObject = new JSONObject();
            String role = Objects.equals(msg.getInitiator(), "1") ? "assistant" : "user";
            jsonObject.put("role", role);
            jsonObject.put("content", msg.getContent());
            String message = jsonObject.toString();
//            String message = "{\"role\": \"" + role + "\", \"content\": \"" + msg.getContent() + "\"}";
            if (i != msgList.size() - 1) {
                res = res + message + ",";
            } else {
                res = res + message;
            }
        }
//        for (Msg msg : msgList) {
//            String role = Objects.equals(msg.getInitiator(), "1") ? "assistant" : "user";
//            String message = "{\"role\": \"" + role + "\", \"content\": \"" + msg.getContent() + "\"}";
//            if (msg != msgList.get(msgList.size() - 1)) {
//                res = res + message + ",";
//            } else {
//                res = res + message;
//            }
//        }
        return res;
    }

}
