package com.clyl.cloudlaw.service.Impl;


import com.clyl.cloudlaw.entity.Msg;
import com.clyl.cloudlaw.entity.Talk;
import com.clyl.cloudlaw.entity.resp.RestBean;
import com.clyl.cloudlaw.mapper.TalkMapper;
import com.clyl.cloudlaw.service.TalkService;
import com.clyl.cloudlaw.util.ChatUtil;
import com.clyl.cloudlaw.util.SnowflakeIdGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author xiugou798
 */
@Slf4j
@Service
public class TalkServiceImpl implements TalkService {

    @Resource
    private TalkMapper talkMapper;

    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;

    @Resource
    private ChatUtil chatUtil;


    @Override
    public RestBean<List<Talk>> getTalkList(String userId) {
        List<Talk> talkList = talkMapper.getTalkList(userId);
        return talkList.size() > 0
                ? new RestBean<>(200, "查询成功", talkList)
                : new RestBean<>(401, "已经没有数据了", null);
    }

    @Override
    public RestBean<Talk> newTalk(Talk talk) {
        talk.setTalkId(snowflakeIdGenerator.nextId());
        int i = talkMapper.newTalk(talk);
        return i == 0
                ? new RestBean<>(401, "添加失败", null)
                : new RestBean<>(200, "添加成功", talk);
    }

    @Override
    public RestBean<List<Msg>> getMsg(String talkId) {
        List<Msg> talkList = talkMapper.getMsgs(talkId);
        return talkList.size() > 0
                ? new RestBean<>(200, "查询成功", talkList)
                : new RestBean<>(401, "已经没有数据了", null);
    }

    @Override
    public RestBean<Msg> sendMsg(Msg msg) throws IOException {
        int i = talkMapper.addPeopleMsg(msg);
        int i1 = talkMapper.addCount(msg);
        System.out.println(msg);
        if (i == 0 || i1 == 0) {
            return new RestBean<>(401, "发送失败", null);
        }
        List<Msg> msgList = getMsg(msg.getTalkId()).getData();
        String messageFormat = chatUtil.formatMessage(msgList);
        String s = chatUtil.sendAllMsg(messageFormat, msg.getContent());
        msg.setContent(s);
        msg.setInitiator("1");
        int j = talkMapper.addChatMsg(msg);
        int j1 = talkMapper.addCount(msg);
        if (j == 0 || j1 == 0) {
            return new RestBean<>(401, "发送失败", null);
        }
        return new RestBean<>(200, "发送成功", msg);
    }

    @Override
    public RestBean<Talk> delTalk(Talk talk) {
        int i = talkMapper.delTalk(talk);
        return i == 0
                ? new RestBean<>(401, "删除失败", null)
                : new RestBean<>(200, "删除成功", talk);
    }
}
