package com.clyl.cloudlaw.controller;


import com.clyl.cloudlaw.entity.Msg;
import com.clyl.cloudlaw.entity.Talk;
import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.service.TalkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/talk")
public class TalkController {

    @Resource
    private TalkService talkService;

    @GetMapping("/getTalks")
    public RestBean<List<Talk>> getTalkList(@RequestParam("userId") String userId) {
        return talkService.getTalkList(userId);
    }

    @PostMapping("/newTalk")
    public RestBean<Talk> newTalk(@RequestBody Talk talk) {
        return talkService.newTalk(talk);
    }

    @PostMapping("/delTalk")
    public RestBean<Talk> delTalk(@RequestBody Talk talk) {
        return talkService.delTalk(talk);
    }



    @GetMapping("/getMsg")
    public RestBean<List<Msg>> getMsg(@RequestParam("talkId") String talkId) {
        return talkService.getMsg(talkId);
    }

    @PostMapping("/sendMsg")
    public RestBean<Msg> sendMsg(@RequestBody Msg msg) throws IOException {
        return talkService.sendMsg(msg);
    }
}
